package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQAnswersEntry;
import com.example.nive_pt1681.recipeapplication.model.Question;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 26/03/18.
 */

public class FAQDetailAdapter extends RecyclerView.Adapter<FAQDetailAdapter.FAQDetailHolder> {

    private Context mContext;
    private Cursor mCursor;
    private Question mQuestion;

    public FAQDetailAdapter(Cursor cursor, Context context){
        mContext=context;
        mCursor=cursor;
    }

    @Override
    public FAQDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.single_answer_item,parent,false);
        return new FAQDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(FAQDetailHolder holder, int position) {
        if(mCursor.moveToPosition(position)){

            Cursor cursor=null;

            try{
                Question question=new Question();
                question.setPostedDate(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(FAQAnswersEntry.ANSWERS_DATE))));
                question.setPostedQuestion(mCursor.getString(mCursor.getColumnIndex(FAQAnswersEntry.ANSWERS_ANSWER)));
                question.setQuestionId(UUID.fromString(mCursor.getString(mCursor.getColumnIndex(FAQAnswersEntry.ANSWERS_ID))));
                if(question.getPostedQuestion().equals("#image#")){
                    Log.d("me", "onBindViewHolder: #image#");
                    cursor=mContext.getContentResolver().query(RecipeContract.ImageEntry.CONTENT_URI,null, RecipeContract.ImageEntry.IMAGES_ANSWER_ID+"=?",new String[]{question.getQuestionId().toString()}, RecipeContract.ImageEntry._ID);
                    if(cursor!=null && cursor.moveToFirst()){
                        question.setQuestionImage(cursor.getString(cursor.getColumnIndex(RecipeContract.ImageEntry.IMAGES_URI)));
                    }
                }

                cursor=mContext.getContentResolver().query(RecipeContract.UserEntry.CONTENT_URI,null, RecipeContract.UserEntry.USER_ID+"=?",new String[]{mCursor.getString(mCursor.getColumnIndex(FAQAnswersEntry.ANSWERS_USER_ID))}, null);
                if(cursor!=null && cursor.moveToFirst()){
                    question.setPostedByUserName(cursor.getString(cursor.getColumnIndex(RecipeContract.UserEntry.USER_NAME)));
                    question.setPostedByUserImage(cursor.getString(cursor.getColumnIndex(RecipeContract.UserEntry.USER_IMAGE)));
                }
                holder.bind(question);
            }
            finally {
                cursor.close();
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }

    public class FAQDetailHolder extends RecyclerView.ViewHolder{
        public TextView questionText,postedUserName,postedDate;
        public ImageView questionImage,answerImage;

        public FAQDetailHolder(View itemView) {
            super(itemView);
            questionText=itemView.findViewById(R.id.answer_text);
            postedUserName=itemView.findViewById(R.id.answer_posted_by_user_name);
            postedDate=itemView.findViewById(R.id.answer_posted_date);
            questionImage=itemView.findViewById(R.id.answer_posted_by_user_image);
            answerImage=itemView.findViewById(R.id.answer_image);
        }

        public void bind(Question question){
            mQuestion=question;
            if(question.getQuestionImage()!=null){
                Glide.with(mContext)
                        .load(question.getQuestionImage())
                        .into(answerImage);
                questionText.setVisibility(View.GONE);
            }
            else{
                answerImage.setVisibility(View.GONE);
                questionText.setText(mQuestion.getPostedQuestion());
            }
            postedUserName.setText(mQuestion.getPostedByUserName());
            postedDate.setText(DateFormat.format("hh:mm dd/MM/yyyy",new Date(mQuestion.getPostedDate())));
            if(mQuestion.getPostedByUserImage()!=null)
                Glide.with(mContext)
                        .load(mQuestion.getPostedByUserImage())
                        .into(questionImage);
            else
                questionImage.setImageResource(R.drawable.default_person);

        }
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }
        mCursor = newCursor;
        if (mCursor != null) {
            notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
    }
}
