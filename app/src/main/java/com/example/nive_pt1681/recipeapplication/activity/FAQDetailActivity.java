package com.example.nive_pt1681.recipeapplication.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.adapter.FAQDetailAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQAnswersEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQQuestionsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;

import java.util.Date;
import java.util.UUID;


/**
 * Created by nive-pt1681 on 26/03/18.
 */

public class FAQDetailActivity extends AppCompatActivity {

    private TextView questionPosted,postedUserName,postedDate;
    private ImageView postedUserImage,questionImage;
    private RecyclerView mRecyclerView;
    private String user_id;
    private FAQDetailAdapter mAdapter;
    private EditText answerText;
    private ImageButton sendAnswer;
    private ImageButton attachAnswer;
    public static final int RESULT_PICK_IMAGE=1;
    private String questionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_detail_layout);

        questionPosted=findViewById(R.id.question_text);
        postedUserImage=findViewById(R.id.question_posted_user_image);
        postedUserName=findViewById(R.id.question_posted_user_name);
        postedDate=findViewById(R.id.question_posted_date);
        mRecyclerView=findViewById(R.id.faq_answers_list);
        questionImage=findViewById(R.id.question_image);
        answerText=findViewById(R.id.answer_by_user);
        sendAnswer=findViewById(R.id.send_answer_button);
        attachAnswer=findViewById(R.id.attach_answer_photo);

        questionId=getIntent().getStringExtra("question_id");
        Cursor cursor=getContentResolver().query(FAQQuestionsEntry.CONTENT_URI,null,FAQQuestionsEntry.QUESTIONS_ID+"=?",new String[]{questionId},null);
        if(cursor!=null && cursor.moveToFirst()){
            questionPosted.setText(cursor.getString(cursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_QUESTION)));
            postedDate.setText(DateFormat.format("hh:mm dd/MM/yyyy",new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_DATE))))));
            user_id=cursor.getString(cursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_USER_ID));
        }
        cursor=getContentResolver().query(UserEntry.CONTENT_URI,null, UserEntry.USER_ID+"=?",new String[]{user_id},null);
        if(cursor!=null && cursor.moveToFirst()){
            postedUserName.setText(cursor.getString(cursor.getColumnIndex(UserEntry.USER_NAME)));
            String user_image=cursor.getString(cursor.getColumnIndex(UserEntry.USER_IMAGE));
            Glide.with(this)
                    .load(user_image)
                    .apply(new RequestOptions()
                    .error(R.drawable.default_person)
                    .placeholder(R.drawable.default_person))
                    .into(postedUserImage);
        }
        cursor=getContentResolver().query(ImageEntry.CONTENT_URI,null,ImageEntry.IMAGES_CATEGORY+"=? AND "+ImageEntry.IMAGES_QUESTION+"=?",new String[]{String.valueOf(ImageEntry.CATEGORY_QUESTIONS),questionId},null);
        if(cursor!=null && cursor.moveToFirst()){
            Glide.with(this)
                    .load(cursor.getString(cursor.getColumnIndex(ImageEntry.IMAGES_URI)))
                    .into(questionImage);
        }
        else{
            questionImage.setVisibility(View.GONE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new FAQDetailAdapter(null,this);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(), FAQAnswersEntry.CONTENT_URI,null,FAQAnswersEntry.ANSWERS_QUESTION_ID+"=?",new String[]{questionId},FAQAnswersEntry.ANSWERS_DATE+" DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });

        sendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(answerText.getText().toString())){
                    ContentValues contentValues=new ContentValues();
                    Cursor cursor1=getContentResolver().query(FAQAnswersEntry.CONTENT_URI,new String[]{"MAX("+FAQAnswersEntry._ID+") AS MAX_ID"},null,null,null);
                    if(cursor1!=null && cursor1.moveToFirst()){
                        contentValues.put(FAQAnswersEntry._ID,cursor1.getInt(0)+1);
                        contentValues.put(FAQAnswersEntry.ANSWERS_QUESTION_ID,questionId);
                        contentValues.put(FAQAnswersEntry.ANSWERS_DATE,String.valueOf(new Date().getTime()));
                        contentValues.put(FAQAnswersEntry.ANSWERS_ANSWER,answerText.getText().toString());
                        contentValues.put(FAQAnswersEntry.ANSWERS_USER_ID,HomeActivity.id);
                        String answer_id=UUID.randomUUID().toString();
                        contentValues.put(FAQAnswersEntry.ANSWERS_ID, answer_id);
                        Uri uri=getContentResolver().insert(FAQAnswersEntry.CONTENT_URI,contentValues);
                        if(uri!=null){
                            //Toast.makeText(getBaseContext(),"Inserted "+answer_id,Toast.LENGTH_SHORT).show();
                            answerText.setText("");
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        attachAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RESULT_PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_PICK_IMAGE){
            if(resultCode==RESULT_OK){
                ContentValues contentValues=new ContentValues();
                Cursor cursor1=getContentResolver().query(FAQAnswersEntry.CONTENT_URI,new String[]{"MAX("+FAQAnswersEntry._ID+") AS MAX_ID"},null,null,null);
                if(cursor1!=null && cursor1.moveToFirst()){
                    contentValues.put(FAQAnswersEntry._ID,cursor1.getInt(0)+1);
                    contentValues.put(FAQAnswersEntry.ANSWERS_QUESTION_ID,questionId);
                    contentValues.put(FAQAnswersEntry.ANSWERS_DATE,String.valueOf(new Date().getTime()));
                    contentValues.put(FAQAnswersEntry.ANSWERS_ANSWER,"#image#");
                    contentValues.put(FAQAnswersEntry.ANSWERS_USER_ID,HomeActivity.id);
                    String answer_id=UUID.randomUUID().toString();
                    contentValues.put(FAQAnswersEntry.ANSWERS_ID, answer_id);
                    Uri uri=getContentResolver().insert(FAQAnswersEntry.CONTENT_URI,contentValues);
                    if(uri!=null){
                        //Toast.makeText(getBaseContext(),"Inserted "+answer_id,Toast.LENGTH_SHORT).show();
                        Log.d("me", "onActivityResult: "+answer_id);
                    }
                    contentValues=new ContentValues();
                    cursor1=getContentResolver().query(ImageEntry.CONTENT_URI,new String[]{"MAX("+FAQAnswersEntry._ID+") AS MAX_ID"},null,null,null);
                    if(cursor1!=null && cursor1.moveToFirst()){
                        contentValues.put(ImageEntry._ID,cursor1.getInt(0)+1);
                        contentValues.put(ImageEntry.IMAGES_ID,UUID.randomUUID().toString());
                        contentValues.put(ImageEntry.IMAGES_URI,data.getData().toString());
                        contentValues.put(ImageEntry.IMAGES_CATEGORY,ImageEntry.CATEGORY_ANSWERS);
                        contentValues.put(ImageEntry.IMAGES_ANSWER_ID,answer_id);
                        uri=getContentResolver().insert(ImageEntry.CONTENT_URI,contentValues);
                        if(uri!=null){
                            //Toast.makeText(getBaseContext(),"Inserted "+answer_id,Toast.LENGTH_SHORT).show();
                            Log.d("me", "onActivityResult: "+answer_id);
                            answerText.setText("");
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
