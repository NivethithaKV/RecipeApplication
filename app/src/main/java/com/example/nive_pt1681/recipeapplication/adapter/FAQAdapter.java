package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.FAQDetailActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQQuestionsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;
import com.example.nive_pt1681.recipeapplication.model.Question;

import java.util.Date;
import java.util.UUID;
/**
 * Created by nive-pt1681 on 25/03/18.
 */

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQHolder> implements Filterable{

    private Cursor mCursor;
    private Context mContext;
    private Cursor initialCursor;
    private Cursor filteredCursor;
    private Question mQuestion;
    private DataSetObserver mDataSetObserver;
    protected MyFilterClass mMyFilterClass;


    public FAQAdapter(Cursor cursor, Context context){
        mCursor=cursor;
        mContext=context;
        mDataSetObserver=new NotifyDataSetObserver();
        if(mCursor!=null)
            mCursor.registerDataSetObserver(mDataSetObserver);
    }


    private class NotifyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChanged();
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public void swapCursor(Cursor newCursor){
        if(newCursor==mCursor){
            return;
        }
        mCursor=newCursor;
        if(mCursor!=null){
            notifyDataSetChanged();
        }
        else{
            notifyDataSetChanged();
        }
    }

    @Override
    public FAQHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.single_faq_layout,parent,false);
        return new FAQHolder(view);
    }

    @Override
    public void onBindViewHolder(FAQHolder holder, int position) {
        if(mCursor.moveToPosition(position)){

            Question question=new Question();
            question.setPostedDate(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_DATE))));
            question.setPostedQuestion(mCursor.getString(mCursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_QUESTION)));
            question.setQuestionId(UUID.fromString(mCursor.getString(mCursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_ID))));

            Cursor cursor=mContext.getContentResolver().query(UserEntry.CONTENT_URI,null, UserEntry.USER_ID+"=?",new String[]{mCursor.getString(mCursor.getColumnIndex(FAQQuestionsEntry.QUESTIONS_USER_ID))}, null);
            if(cursor!=null && cursor.moveToFirst()){
                question.setPostedByUserName(cursor.getString(cursor.getColumnIndex(UserEntry.USER_NAME)));
                question.setPostedByUserImage(cursor.getString(cursor.getColumnIndex(UserEntry.USER_IMAGE)));
            }
            holder.bind(question);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(mMyFilterClass==null){
            return new MyFilterClass();
        }
        return mMyFilterClass;
    }

    @Override
    public long getItemId(int position) {
        if(mCursor!=null && mCursor.moveToPosition(position)){
            return Long.getLong(mCursor.getString(mCursor.getColumnIndex(FAQQuestionsEntry._ID)));
        }
        return 0;
    }

    private class MyFilterClass extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                filteredCursor=mContext.getContentResolver().query(FAQQuestionsEntry.CONTENT_URI,null, FAQQuestionsEntry.QUESTIONS_QUESTION+" LIKE ?",new String[]{"%"+constraint+"%"},null);
                if(filteredCursor==null)
                    Log.d("me", "performFiltering: null returned");
                results.count=filteredCursor.getCount();
                results.values=filteredCursor;
            }
            else if(constraint==null || constraint.length()==0){
                Log.d("me", "performFiltering: length 0 and null i guess!!!");
                results.count=initialCursor.getCount();
                results.values=initialCursor;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCursor=(Cursor)results.values;
            notifyDataSetChanged();
        }
    }

    public void setInitialCursor(Cursor cursor){
        initialCursor=cursor;
    }

    public class FAQHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView questionText,postedUserName,postedDate;
        private String id;
        public ImageView questionImage;

        public FAQHolder(View itemView) {
            super(itemView);
            questionText=itemView.findViewById(R.id.question_text);
            postedUserName=itemView.findViewById(R.id.question_posted_user_name);
            postedDate=itemView.findViewById(R.id.question_posted_date);
            questionImage=itemView.findViewById(R.id.question_posted_user_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Question question){
            mQuestion=question;
            id=mQuestion.getQuestionId().toString();
            questionText.setText(mQuestion.getPostedQuestion());
            postedUserName.setText(mQuestion.getPostedByUserName());
            postedDate.setText(DateFormat.format("hh:mm dd/MM/yyyy",new Date(mQuestion.getPostedDate())));
            if(mQuestion.getPostedByUserImage()!=null)
                Glide.with(mContext)
                        .load(mQuestion.getPostedByUserImage())
                        .into(questionImage);
            else
                questionImage.setImageResource(R.drawable.default_person);
        }

        @Override
        public void onClick(View v) {
            Log.d("me", "onClick: in FAQ");
            Intent intent=new Intent(mContext.getApplicationContext(), FAQDetailActivity.class);
            intent.putExtra("question_id",id);
            mContext.startActivity(intent);
        }
    }
}
