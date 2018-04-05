package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import com.example.nive_pt1681.recipeapplication.activity.RecipeDetailActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.model.Recipe;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 07/03/18.
 */

public class RecipeHomeRecyclerAdapter extends RecyclerView.Adapter<RecipeHomeRecyclerAdapter.RecipeHomeRecyclerHolder> implements Filterable {

    private boolean mDataValid;
    private Cursor mCursor;
    private Context mContext;
    private Cursor initialCursor;
    private Cursor filteredCursor;
    private int id;
    private Recipe mRecipe;
    private DataSetObserver mDataSetObserver;
    protected MyFilterClass mMyFilterClass;


    public RecipeHomeRecyclerAdapter(Cursor cursor, Context context){
        mCursor=cursor;
        mContext=context;
        mDataValid=mCursor!=null;
        id=mDataValid? mCursor.getInt(mCursor.getColumnIndex(RecipeEntry._ID)):-1;
        mDataSetObserver=new NotifyDataSetObserver();
        if(mCursor!=null)
            mCursor.registerDataSetObserver(mDataSetObserver);
    }


    private class NotifyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid=true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid=false;
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
            id=newCursor.getColumnIndex(RecipeEntry._ID);
            mDataValid=true;
            notifyDataSetChanged();
        }
        else{
            id=-1;
            mDataValid=false;
            notifyDataSetChanged();
        }
    }

    @Override
    public RecipeHomeRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recipe_in_cards,parent,false);
        return new RecipeHomeRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHomeRecyclerHolder holder, int position) {
        if(mCursor.moveToPosition(position)){
            id=mCursor.getInt(mCursor.getColumnIndex(RecipeEntry._ID));

            Recipe recipe=RecipeDetailActivity.cursorToRecipe(mCursor);
            Cursor cursor=mContext.getContentResolver().query(ImageEntry.CONTENT_URI,null,ImageEntry.IMAGES_RECIPE_ID+"=?",new String[]{recipe.getRecipe_id().toString()},ImageEntry._ID);
            if(cursor!=null && cursor.moveToFirst()){
                recipe.setRecipe_image(cursor.getString(cursor.getColumnIndex(ImageEntry.IMAGES_URI)));
            }
            holder.bind(recipe);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataValid && mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(mMyFilterClass==null){
            return new RecipeHomeRecyclerAdapter.MyFilterClass();
        }
        return mMyFilterClass;
    }

    @Override
    public long getItemId(int position) {
        if(mDataValid && mCursor!=null && mCursor.moveToPosition(position)){
            return mCursor.getLong(id);
        }
        return 0;
    }

    private class MyFilterClass extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                String sortOrder=RecipeEntry.RECIPE_EARNED_CREDITS+" DESC";
                filteredCursor=mContext.getContentResolver().query(RecipeEntry.CONTENT_URI,null, RecipeEntry.RECIPE_TITLE+" LIKE ?",new String[]{"%"+constraint+"%"},sortOrder);
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



    public class RecipeHomeRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView recipeTitle,recipeEarnedCredits,recipeCookingTime;
        private String id;
        public ImageView recipeImage;

        public RecipeHomeRecyclerHolder(View itemView) {
            super(itemView);
            recipeTitle=itemView.findViewById(R.id.recipe_title);
            recipeEarnedCredits=itemView.findViewById(R.id.recipe_earned_credits);
            recipeCookingTime=itemView.findViewById(R.id.recipe_cooking_time);
            recipeImage=itemView.findViewById(R.id.recipe_thumbnail);
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe){
            mRecipe=recipe;
            id=mRecipe.getRecipe_id().toString();
            recipeTitle.setText(mRecipe.getRecipe_title());
            recipeCookingTime.setText(mRecipe.getRecipe_cooking_time());
            recipeEarnedCredits.setText(String.valueOf(mRecipe.getRecipe_earned_credits())+" Credits");
            if(mRecipe.getRecipe_image()!=null)
                Glide.with(mContext)
                .load(mRecipe.getRecipe_image())
                .into(recipeImage);
            else
                recipeImage.setImageResource(R.drawable.default_person);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext.getApplicationContext(), RecipeDetailActivity.class);
            intent.putExtra("recipe_id",id);
            if(mRecipe.getRecipe_id()==null)
                Log.d("me","onClick : recipe id null");
            else
                Log.d("me", "onClick: "+mRecipe.getRecipe_id());
            mContext.startActivity(intent);
        }
    }

}
