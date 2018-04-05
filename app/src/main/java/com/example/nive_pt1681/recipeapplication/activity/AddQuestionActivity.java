package com.example.nive_pt1681.recipeapplication.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQQuestionsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 25/03/18.
 */

public class AddQuestionActivity extends AppCompatActivity {

    private EditText questionText;
    private Button addQuestionButton;
    private Button addQuestionImage;
    private LinearLayout parentToAddImage;
    private ImageView questionImage,deleteQuestionImage;
    public static final int RESULT_PICK_IMAGE=0;
    private String imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_add_question_layout);

        questionText=findViewById(R.id.new_question_text);
        addQuestionButton=findViewById(R.id.add_question_button);
        parentToAddImage=findViewById(R.id.add_image_in_question);
        addQuestionImage=findViewById(R.id.add_question_image);

        addQuestionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View view=inflater.inflate(R.layout.add_method_image,null);
                questionImage=view.findViewById(R.id.method_image);
                deleteQuestionImage=view.findViewById(R.id.delete_method_image);
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RESULT_PICK_IMAGE);
                parentToAddImage.addView(view,parentToAddImage.getChildCount());
            }
        });
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues=new ContentValues();
                Cursor cursor=null;
                try{
                    cursor=getContentResolver().query(FAQQuestionsEntry.CONTENT_URI,new String[]{"MAX("+ FAQQuestionsEntry._ID+") AS MAX_ID"},null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        int count=cursor.getInt(0);
                        contentValues.put(FAQQuestionsEntry._ID,count+1);
                        contentValues.put(FAQQuestionsEntry.QUESTIONS_DATE,String.valueOf(new Date().getTime()));
                        String question_id=UUID.randomUUID().toString();
                        contentValues.put(FAQQuestionsEntry.QUESTIONS_ID, question_id);
                        contentValues.put(FAQQuestionsEntry.QUESTIONS_USER_ID,HomeActivity.id);
                        contentValues.put(FAQQuestionsEntry.QUESTIONS_QUESTION,questionText.getText().toString());
                        Uri uri=getContentResolver().insert(FAQQuestionsEntry.CONTENT_URI,contentValues);
                        if(imageUri!=null){
                            cursor=getContentResolver().query(ImageEntry.CONTENT_URI,new String[]{"MAX("+ ImageEntry._ID+") AS MAX_ID"},null,null,null);
                            if(cursor!=null && cursor.moveToFirst()){
                                count=cursor.getInt(0);
                                contentValues=new ContentValues();
                                contentValues.put(ImageEntry._ID,count+1);
                                contentValues.put(ImageEntry.IMAGES_ID,UUID.randomUUID().toString());
                                contentValues.put(ImageEntry.IMAGES_CATEGORY,ImageEntry.CATEGORY_QUESTIONS);
                                contentValues.put(ImageEntry.IMAGES_QUESTION,question_id);
                                contentValues.put(ImageEntry.IMAGES_URI,imageUri);
                                uri=getContentResolver().insert(ImageEntry.CONTENT_URI,contentValues);
                            }
                        }
                        if(uri!=null){
                            Toast.makeText(AddQuestionActivity.this,"Inserted Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                finally {
                    cursor.close();
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_PICK_IMAGE){
            if(resultCode==RESULT_OK){
                if(data.getData()!=null){
                    imageUri=data.getData().toString();
                    Glide.with(this)
                            .load(data.getData())
                            .into(questionImage);
                    addQuestionImage.setVisibility(View.GONE);
                }
            }
            else{
                questionImage.setVisibility(View.GONE);
                deleteQuestionImage.setVisibility(View.GONE);
            }
        }
    }

    public void deleteMethodImage(View view) {
        parentToAddImage.removeView((View)view.getParent());
        addQuestionImage.setVisibility(View.VISIBLE);
    }
}
