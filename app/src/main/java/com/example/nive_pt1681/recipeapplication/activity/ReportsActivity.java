package com.example.nive_pt1681.recipeapplication.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReportsEntry;
import com.example.nive_pt1681.recipeapplication.model.Recipe;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 16/03/18.
 */

public class ReportsActivity extends AppCompatActivity {

    private AutoCompleteTextView regarding;
    private EditText problem;
    private RatingBar vulnerability;
    private ImageView mImageView;
    private Button report_button;
    private Button add_image_button;
    private String imageUri=null;
    private String[] categories={"Info about recipe","Ingredients","Methods","Video","Images"};
    public static final int RESULT_LOAD_IMAGE=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_report_page);

        problem=findViewById(R.id.report_problem);
        vulnerability=findViewById(R.id.vulnerability);
        report_button=findViewById(R.id.report);
        add_image_button=findViewById(R.id.add_report_image_button);
        mImageView=findViewById(R.id.reported_photo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        regarding = findViewById(R.id.report_regarding);
        regarding.setAdapter(adapter);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String recipe_id=getIntent().getStringExtra("recipe_id");

        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMAGE);
            }
        });

        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues=new ContentValues();


                Cursor cursor=null;
                int count=0;
                if(TextUtils.isEmpty(regarding.getText().toString())){
                    regarding.setError("This field cannot be empty!");
                }
                else if(TextUtils.isEmpty(problem.getText().toString())){
                    problem.setError("This field cannot be empty!");
                }
                else if(vulnerability.getRating()==0){
                    TextView textView=findViewById(R.id.vulnerability_text);
                    textView.setError("Rate the error for better performance");
                }
                else{
                    try{
                        cursor=getContentResolver().query(ReportsEntry.CONTENT_URI,new String[]{"MAX("+ ReportsEntry._ID+") AS max_id"},null,null,null);
                        if(cursor!=null && cursor.moveToFirst()){
                            count=cursor.getInt(0);
                        }
                        contentValues.put(ReportsEntry._ID,count+1);
                        contentValues.put(ReportsEntry.REPORTS_RECIPE_ID,recipe_id);
                        long time=new Date().getTime();
                        contentValues.put(ReportsEntry.REPORTS_DATE,time);
                        contentValues.put(ReportsEntry.REPORTS_PROBLEM,problem.getText().toString());
                        contentValues.put(ReportsEntry.REPORTS_REGARDING,regarding.getText().toString());
                        String reports_id=UUID.randomUUID().toString();
                        contentValues.put(ReportsEntry.REPORTS_ID, reports_id);
                        contentValues.put(ReportsEntry.REPORTS_USER_ID,HomeActivity.id);
                        contentValues.put(ReportsEntry.REPORTS_VULNERABILITY,vulnerability.getRating());
                        Uri uri=getContentResolver().insert(ReportsEntry.CONTENT_URI,contentValues);
                        if(imageUri!=null){
                            contentValues=new ContentValues();
                            Cursor cursor1=getContentResolver().query(ImageEntry.CONTENT_URI,new String[]{"MAX("+ImageEntry._ID+") AS MAX_ID"},null,null,null);
                            if(cursor1!=null && cursor1.moveToFirst()){
                                int id=cursor1.getInt(0);
                                contentValues.put(ImageEntry._ID,id+1);
                            }
                            contentValues.put(ImageEntry.IMAGES_ID,UUID.randomUUID().toString());
                            contentValues.put(ImageEntry.IMAGES_CATEGORY, ImageEntry.CATEGORY_REPORTS);
                            contentValues.put(ImageEntry.IMAGES_URI,imageUri);
                            contentValues.put(ImageEntry.IMAGES_REPORT_ID,reports_id);
                            contentValues.put(ImageEntry.IMAGES_RECIPE_ID,recipe_id);
                        }
                        Uri uri1=getContentResolver().insert(ImageEntry.CONTENT_URI,contentValues);
                        if(uri!=null && uri1!=null){
                            Toast.makeText(ReportsActivity.this,"Reported Successfully...",Toast.LENGTH_SHORT).show();
                            ContentValues values=new ContentValues();
                            cursor=getContentResolver().query(NotificationEntry.CONTENT_URI,new String[]{"MAX(" + RecipeContract.FavoritesEntry._ID + ") AS max_id"}, null, null, null);
                            if(cursor!=null && cursor.moveToFirst()){
                                count=cursor.getInt(0);
                                values.put(NotificationEntry._ID,count+1);
                                values.put(NotificationEntry.NOTIFICATION_CATEGORY, NotificationEntry.CATEGORY_REPORT);
                                values.put(NotificationEntry.NOTIFICATION_ID, UUID.randomUUID().toString());
                                cursor=getContentResolver().query(RecipeEntry.CONTENT_URI,new String[]{RecipeEntry.RECIPE_BY_USER_ID,RecipeEntry.RECIPE_TITLE}, RecipeEntry.RECIPE_ID+"=?",new String[]{recipe_id},null);
                                values.put(NotificationEntry.NOTIFICATION_TO_USER_ID,cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_BY_USER_ID)));
                                values.put(NotificationEntry.NOTIFICATION_MESSAGE,"Your recipe "+cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_TITLE))+" has been reported!!!");
                                getContentResolver().insert(NotificationEntry.CONTENT_URI,values);
                            }
                        }
                        else{
                            Toast.makeText(ReportsActivity.this,"Some problem occurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                    finally {
                        cursor.close();
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            if (selectedImg != null) {
                Glide.with(this)
                        .load(selectedImg)
                        .into(mImageView);
                imageUri=selectedImg.toString();
                add_image_button.setVisibility(View.GONE);
            }
        }
    }

}
