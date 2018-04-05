package com.example.nive_pt1681.recipeapplication.activity;

import android.arch.lifecycle.ReportFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.adapter.ImageAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FavoritesEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.LikesEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.fragment.DatePickerFragment;
import com.example.nive_pt1681.recipeapplication.fragment.DetailRecipeFragment;
import com.example.nive_pt1681.recipeapplication.fragment.LeftBarFragment;
import com.example.nive_pt1681.recipeapplication.fragment.RecipeCommentsFragment;
import com.example.nive_pt1681.recipeapplication.fragment.RecipeIngredientsFragment;
import com.example.nive_pt1681.recipeapplication.fragment.RecipeMethodsFragment;
import com.example.nive_pt1681.recipeapplication.fragment.RecipeReportsFragment;
import com.example.nive_pt1681.recipeapplication.fragment.RecipeVideoFragment;
import com.example.nive_pt1681.recipeapplication.model.Recipe;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 08/03/18.
 */

public class RecipeDetailActivity extends AppCompatActivity implements LeftBarFragment.myScrollListener{
    private String recipe_id;
    public ImageButton likeButton;
    private Cursor mCursor;
    public static Context mContext;
    private static boolean favoritesMarked=false;
    private static boolean likeMarked=false;
    private NestedScrollView scrollView;
    private int infoHeight;
    private int ingredientHeight;
    private int methodsHeight;
    private int videoHeight;
    private int reportsHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_layout);
        mContext=getApplicationContext();
        recipe_id=getIntent().getStringExtra("recipe_id");
        Log.d("me", "onCreate: "+recipe_id);

        Toolbar toolbar=findViewById(R.id.my_account_toolbar);
        mCursor=getContentResolver().query(RecipeEntry.CONTENT_URI,null,RecipeEntry.RECIPE_ID+"=?",new String[]{recipe_id},null);
        if(mCursor!=null && mCursor.moveToFirst())
            toolbar.setTitle(mCursor.getString(mCursor.getColumnIndex(RecipeEntry.RECIPE_TITLE)));
        else
            toolbar.setTitle("DetailView");
        final Recipe recipe=cursorToRecipe(mCursor);
        likeButton=findViewById(R.id.like_button);
        ImageButton shareButton=findViewById(R.id.share_button);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,recipe.getRecipe_id().toString());
                intent=Intent.createChooser(intent,"Share your recipe via");
                startActivity(intent);
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!likeMarked){
                    Cursor cursor=null;
                    try {
                        cursor= getContentResolver().query(LikesEntry.CONTENT_URI, new String[]{"MAX(" + FavoritesEntry._ID + ") AS max_id"}, null, null, null);
                        ContentValues contentValues = new ContentValues();
                        int count = 0;
                        if(cursor!=null && cursor.moveToFirst()){
                            count=cursor.getInt(0);
                        }
                        contentValues.put(LikesEntry._ID,count+1);
                        contentValues.put(LikesEntry.LIKES_USER_ID,HomeActivity.id);
                        contentValues.put(LikesEntry.LIKES_RECIPE_ID,recipe_id);
                        Uri uri=getContentResolver().insert(LikesEntry.CONTENT_URI,contentValues);
                        if (uri != null) {
                            likeButton.setImageDrawable(getDrawable(R.drawable.ic_favorites));
                            likeMarked=true;
                            ContentValues values=new ContentValues();
                            int credits=recipe.getRecipe_earned_credits();
                            values.put(RecipeEntry.RECIPE_EARNED_CREDITS,credits+5);
                            recipe.setRecipe_earned_credits(credits+5);
                            getContentResolver().update(RecipeEntry.CONTENT_URI,values,RecipeEntry.RECIPE_ID+"=?",new String[]{recipe.getRecipe_id().toString()});
                            values=new ContentValues();
                            cursor=getContentResolver().query(NotificationEntry.CONTENT_URI,new String[]{"MAX(" + FavoritesEntry._ID + ") AS max_id"}, null, null, null);
                            if(cursor!=null && cursor.moveToFirst()){
                                count=cursor.getInt(0);
                                values.put(NotificationEntry._ID,count+1);
                                values.put(NotificationEntry.NOTIFICATION_CATEGORY,NotificationEntry.CATEGORY_LIKE);
                                values.put(NotificationEntry.NOTIFICATION_ID,UUID.randomUUID().toString());
                                values.put(NotificationEntry.NOTIFICATION_RECIPE_ID,recipe_id);
                                values.put(NotificationEntry.NOTIFICATION_TO_USER_ID,recipe.getRecipe_by_user_id());
                                values.put(NotificationEntry.NOTIFICATION_MESSAGE,"Your Recipe "+recipe.getRecipe_title()+" got one more thumbs UP");
                                getContentResolver().insert(NotificationEntry.CONTENT_URI,values);
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_about_fragment, DetailRecipeFragment.newInstance(recipe)).commit();
                        }
                        else{
                            Toast.makeText(RecipeDetailActivity.this,"Some problem occurred in insertion",Toast.LENGTH_SHORT).show();
                        }
                    }
                    finally {
                        cursor.close();
                    }
                }
                else{
                    int rowsDeleted=getContentResolver().delete(LikesEntry.CONTENT_URI,LikesEntry.LIKES_USER_ID+"=? AND "+LikesEntry.LIKES_RECIPE_ID+"=?",new String[]{HomeActivity.id,recipe_id});
                    if(rowsDeleted==1){
                        likeButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white_24dp));
                        likeMarked=false;
                        ContentValues values=new ContentValues();
                        int credits=recipe.getRecipe_earned_credits();
                        values.put(RecipeEntry.RECIPE_EARNED_CREDITS,credits-5);
                        recipe.setRecipe_earned_credits(credits-5);
                        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_about_fragment, DetailRecipeFragment.newInstance(recipe)).commit();
                        getContentResolver().update(RecipeEntry.CONTENT_URI,values,RecipeEntry.RECIPE_ID+"=?",new String[]{recipe.getRecipe_id().toString()});
                    }
                    else{
                        Toast.makeText(RecipeDetailActivity.this,"Some problem occurred in deletion",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        new loadFragments().execute(recipe);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCursor.close();

        ViewPager viewPager=findViewById(R.id.pager);
        TabLayout tabLayout=findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager);

        mCursor=getContentResolver().query(ImageEntry.CONTENT_URI,null,ImageEntry.IMAGES_RECIPE_ID+"=? AND "+ImageEntry.IMAGES_CATEGORY+" = "+ImageEntry.CATEGORY_RECIPE,new String[]{recipe_id},ImageEntry._ID);
        if(mCursor!=null)
            Log.d("me", "onCreate: "+mCursor.getCount());
        else
            Log.d("me", "onCreate: cursor null");

        PagerAdapter pagerAdapter=new ImageAdapter(this,mCursor);
        viewPager.setAdapter(pagerAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_detail,menu);
        final MenuItem item=menu.findItem(R.id.favorites);
        Cursor cursor=getContentResolver().query(FavoritesEntry.CONTENT_URI,null,FavoritesEntry.FAVORITES_USER_ID+"=? AND "+FavoritesEntry.FAVORITES_RECIPE_ID+"=?",new String[]{HomeActivity.id,recipe_id},null);
        if(cursor!=null && cursor.moveToFirst()){
            item.setIcon(R.drawable.ic_bookmark_white_24dp);
            favoritesMarked=false;
        }
        else{
            item.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            favoritesMarked=true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("me", "onOptionsItemSelected: here");
                this.onBackPressed();
                break;
            case R.id.favorites:
                if(!favoritesMarked){
                    int rowsDeleted=getContentResolver().delete(FavoritesEntry.CONTENT_URI,FavoritesEntry.FAVORITES_USER_ID+"=? AND "+FavoritesEntry.FAVORITES_RECIPE_ID+"=?",new String[]{HomeActivity.id,recipe_id});
                    if(rowsDeleted==0){
                        Toast.makeText(RecipeDetailActivity.this,"Not deleted successfully...",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RecipeDetailActivity.this,"Deleted successfully...",Toast.LENGTH_SHORT).show();
                    }
                    item.setIcon(R.drawable.ic_bookmark_border_white_24dp);
                    favoritesMarked=true;
                }
                else{
                    int count=0;
                    ContentValues contentValues=new ContentValues();
                    Cursor cursor = getContentResolver().query(FavoritesEntry.CONTENT_URI,new String[]{"MAX("+ FavoritesEntry._ID+") AS max_id"},null,null,null);
                    if (cursor != null && cursor.moveToFirst()) {
                        count = cursor.getInt(0);
                        cursor.close();
                    }
                    contentValues.put(FavoritesEntry._ID,count+1);
                    contentValues.put(FavoritesEntry.FAVORITES_RECIPE_ID,recipe_id);
                    contentValues.put(FavoritesEntry.FAVORITES_USER_ID,HomeActivity.id);
                    Uri uri=getContentResolver().insert(FavoritesEntry.CONTENT_URI,contentValues);
                    if(uri==null){
                        Toast.makeText(RecipeDetailActivity.this,"Favorites not marked successfully...",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RecipeDetailActivity.this,"Favorites marked successfully...",Toast.LENGTH_SHORT).show();
                    }
                    item.setIcon(R.drawable.ic_bookmark_white_24dp);
                    favoritesMarked=false;
                }
                break;
            case R.id.report:
                Intent intent=new Intent(this,ReportsActivity.class);
                intent.putExtra("recipe_id",recipe_id);
                startActivity(intent);
                break;
            case R.id.add_to_calendar:
                DialogFragment dialogFragment=DatePickerFragment.newInstance(recipe_id);
                dialogFragment.show(getSupportFragmentManager(),"My date picker");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Recipe cursorToRecipe(Cursor cursor){
        Recipe recipe = new Recipe();
        int id = cursor.getInt(cursor.getColumnIndex(RecipeEntry._ID));
        recipe.setId(id);
        recipe.setRecipe_by_user_id(cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_BY_USER_ID)));
        recipe.setRecipe_id(UUID.fromString(cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_ID))));
        recipe.setRecipe_title(cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_TITLE)));
        recipe.setRecipe_security(cursor.getInt(cursor.getColumnIndex(RecipeEntry.RECIPE_SECURITY)));
        recipe.setRecipe_description(cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_DESCRIPTION)));
        recipe.setRecipe_no_of_servings(cursor.getInt(cursor.getColumnIndex(RecipeEntry.RECIPE_NO_OF_SERVINGS)));
        recipe.setRecipe_cooking_time(cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_COOKING_TIME)));
        recipe.setRecipe_diet(cursor.getInt(cursor.getColumnIndex(RecipeEntry.RECIPE_DIET)));
        recipe.setRecipe_cooking_level(cursor.getInt(cursor.getColumnIndex(RecipeEntry.RECIPE_COOKING_LEVEL)));
        recipe.setRecipe_date(Long.parseLong(cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_DATE))));
        recipe.setRecipe_no_of_likes(cursor.getInt(cursor.getColumnIndex(RecipeEntry.RECIPE_NO_OF_LIKES)));
        recipe.setRecipe_earned_credits(cursor.getInt(cursor.getColumnIndex(RecipeEntry.RECIPE_EARNED_CREDITS)));
        return recipe;
    }

    @Override
    public void scrollToFragment(int position) {
        int dy=0;
        switch (position){
            case 0: break;
            case 1:
                dy+=infoHeight;
                break;
            case 2:
                dy=ingredientHeight;
                break;
            case 3:
                dy=methodsHeight;
                break;
            case 4:
                dy=videoHeight;
                break;
            case 5:
                dy=reportsHeight;
                break;
        }
        scrollView.smoothScrollTo(0,dy);
    }

    private class loadFragments extends AsyncTask<Recipe,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            Cursor cursor1=null;

            try{
                cursor1 = getContentResolver().query(ImageEntry.CONTENT_URI, null, ImageEntry.IMAGES_RECIPE_ID + "=?", new String[]{recipes[0].getRecipe_id().toString()}, ImageEntry._ID);
                if (cursor1 != null && cursor1.moveToFirst()) {
                    recipes[0].setRecipe_image(cursor1.getString(cursor1.getColumnIndex(ImageEntry.IMAGES_URI)));
                }
            }
            finally {
                cursor1.close();
            }

            try{
                cursor1=getContentResolver().query(LikesEntry.CONTENT_URI,null,LikesEntry.LIKES_USER_ID+"=? AND "+LikesEntry.LIKES_RECIPE_ID+"=?",new String[]{HomeActivity.id,recipes[0].getRecipe_id().toString()},null);
                if(cursor1!=null && cursor1.moveToFirst()){
                    likeButton.setImageDrawable(getDrawable(R.drawable.ic_favorites));
                    likeMarked=true;
                }
                else{
                    likeButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white_24dp));
                    likeMarked=false;
                }
            }
            finally {
                cursor1.close();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_about_fragment, DetailRecipeFragment.newInstance(recipes[0])).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_ingredients_layout, RecipeIngredientsFragment.newInstance(recipes[0].getRecipe_id().toString(),recipes[0].getRecipe_no_of_servings())).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_methods_layout, RecipeMethodsFragment.newInstance(recipes[0].getRecipe_id().toString(),recipes[0].getRecipe_cooking_time())).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_reports_layout, RecipeReportsFragment.newInstance(recipes[0].getRecipe_id().toString())).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_video_layout, RecipeVideoFragment.newInstance(recipes[0].getRecipe_id().toString())).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_comments_layout, RecipeCommentsFragment.newInstance(recipes[0].getRecipe_id().toString())).commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            scrollView=findViewById(R.id.nestedScrollView);
            scrollView.pageScroll(View.SCROLL_INDICATOR_TOP);
            //scrollView.requestChildFocus(scrollView.getChildAt(0),null);
            scrollView.smoothScrollTo(0,0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(infoHeight==0){
            FrameLayout frameLayout=findViewById(R.id.recipe_about_fragment);
            infoHeight=frameLayout.getHeight();
            frameLayout=findViewById(R.id.recipe_ingredients_layout);
            ingredientHeight=frameLayout.getHeight();
            frameLayout=findViewById(R.id.recipe_methods_layout);
            methodsHeight=frameLayout.getHeight();
            frameLayout=findViewById(R.id.recipe_video_layout);
            videoHeight=frameLayout.getHeight();
            frameLayout=findViewById(R.id.recipe_reports_layout);
            reportsHeight=frameLayout.getHeight();
        }
    }
}
