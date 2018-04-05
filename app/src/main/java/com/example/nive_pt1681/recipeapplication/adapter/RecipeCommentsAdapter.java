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
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.CommentsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;
import com.example.nive_pt1681.recipeapplication.model.Comment;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 21/03/18.
 */

public class RecipeCommentsAdapter extends RecyclerView.Adapter<RecipeCommentsAdapter.RecipeCommentsHolder>{

    private Cursor mCursor;
    private Context mContext;
    private int id;

    public RecipeCommentsAdapter(Cursor cursor, Context context){
        mCursor=cursor;
        mContext=context;
    }


    public void swapCursor(Cursor newCursor){
        if(newCursor==mCursor){
            return;
        }
        mCursor=newCursor;
        if(mCursor!=null){
            id=newCursor.getColumnIndex(RecipeContract.RecipeEntry._ID);
            notifyDataSetChanged();
        }
        else{
            id=-1;
            notifyDataSetChanged();
        }
    }

    @Override
    public RecipeCommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.single_comment_item,parent,false);
        return new RecipeCommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeCommentsHolder holder, int position) {
        if(mCursor.moveToPosition(position)){

            Comment comment=new Comment();
            comment.setCommentId(UUID.fromString(mCursor.getString(mCursor.getColumnIndex(CommentsEntry.COMMENTS_ID))));
            comment.setRecipeId(mCursor.getString(mCursor.getColumnIndex(CommentsEntry.COMMENTS_RECIPE_ID)));
            comment.setCommentedText(mCursor.getString(mCursor.getColumnIndex(CommentsEntry.COMMENTS_COMMENT)));
            comment.setCommentedDate(new Date(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(CommentsEntry.COMMENTS_DATE)))));

            Cursor cursor=null;
            try{
                if(comment.getCommentedText().equals("#image#")){
                    cursor=mContext.getContentResolver().query(ImageEntry.CONTENT_URI,null, ImageEntry.IMAGES_COMMENT_ID+"=? ",new String[]{comment.getCommentId().toString()}, ImageEntry._ID);
                    if(cursor!=null && cursor.moveToFirst()){
                        Log.d("me", "onBindViewHolder: Entered the #image#");
                        comment.setCommentedImage(cursor.getString(cursor.getColumnIndex(ImageEntry.IMAGES_URI)));
                    }
                }
                String user_id=mCursor.getString(mCursor.getColumnIndex(CommentsEntry.COMMENTS_USER_ID));
                cursor=mContext.getContentResolver().query(UserEntry.CONTENT_URI,new String[]{UserEntry.USER_NAME, UserEntry.USER_ID, UserEntry.USER_IMAGE}, UserEntry.USER_ID+"=?",new String[]{user_id},null);
                if(cursor!=null && cursor.moveToFirst()){
                    comment.setUserName(cursor.getString(cursor.getColumnIndex(UserEntry.USER_NAME)));
                    comment.setUserImage(cursor.getString(cursor.getColumnIndex(UserEntry.USER_IMAGE)));
                }
            }
            finally {
                cursor.close();
            }
            holder.bind(comment);
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
    public long getItemId(int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(id);
        }
        return 0;
    }


    public class RecipeCommentsHolder extends RecyclerView.ViewHolder{
        public TextView commentedUserName,commentedText,commentedDate;
        public ImageView commentedUserImage,commentedImage;

        public RecipeCommentsHolder(View itemView) {
            super(itemView);
            commentedUserName=itemView.findViewById(R.id.commented_user_name);
            commentedText=itemView.findViewById(R.id.comment);
            commentedDate=itemView.findViewById(R.id.commented_date);
            commentedImage=itemView.findViewById(R.id.commented_image);
            commentedUserImage=itemView.findViewById(R.id.commented_user_image);
        }

        public void bind(Comment comment){
            commentedUserName.setText(comment.getUserName());
            if(comment.getCommentedText().equals("#image#")){
                commentedText.setVisibility(View.GONE);
            }
            else
                commentedText.setText(comment.getCommentedText());
            commentedDate.setText(DateFormat.format(" hh:mm:ss dd/MM/yyyy",comment.getCommentedDate()));
            if(comment.getUserImage()!=null)
                Glide.with(mContext)
                        .load(comment.getUserImage())
                        .into(commentedUserImage);
            else
                commentedUserImage.setImageResource(R.drawable.default_person);
            if(comment.getCommentedImage()!=null){
                Glide.with(mContext)
                        .load(comment.getCommentedImage())
                        .into(commentedImage);
            }
            else{
                commentedImage.setVisibility(View.GONE);
            }
        }
    }

}
