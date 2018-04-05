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

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.RecipeDetailActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FavoritesEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.model.Recipe;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 12/03/18.
 */

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.MyRecipesHolder> {

    private Cursor mCursor;
    private Context mContext;
    private int id;
    private Recipe mRecipe;


    public MyRecipesAdapter(Cursor cursor, Context context){
        mCursor=cursor;
        mContext=context;
        id=mCursor!=null? mCursor.getInt(mCursor.getColumnIndex(RecipeEntry._ID)):-1;
    }



    @Override
    public MyRecipesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recipe_in_cards,parent,false);
        return new MyRecipesHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecipesHolder holder, int position) {
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
        if(mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }


    @Override
    public long getItemId(int position) {
        if(mCursor!=null && mCursor.moveToPosition(position)){
            return mCursor.getLong(id);
        }
        return 0;
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
            notifyDataSetChanged();
        }
        else{
            id=-1;
            notifyDataSetChanged();
        }
    }

    public class MyRecipesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView recipeTitle,recipeEarnedCredits,recipeCookingTime;
        private String id;
        public ImageView recipeImage;

        public MyRecipesHolder(View itemView) {
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
                recipeImage.setImageURI(Uri.parse(mRecipe.getRecipe_image()));
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