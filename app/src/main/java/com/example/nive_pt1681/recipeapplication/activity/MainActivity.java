package com.example.nive_pt1681.recipeapplication.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.IngredientsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MealTimeCategoryEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MethodsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReferenceLinkEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReportsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.security.Permission;

/**
 * Created by nive-pt1681 on 06/03/18.
 */

public class MainActivity extends AppCompatActivity{

    public static final String PREFS_ALREADY_LOADED="already_loaded";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new loadDatabases().execute();
    }

    public String loadJSONFromAsset(){
        String jsonData=null;
        try{
            InputStream inputStream=getAssets().open("userjson");
            int size=inputStream.available();
            byte[] bytes=new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            jsonData=new String(bytes,"UTF-8");
        }
        catch (Exception e){
            Log.d("me", "loadJSONFromAsset: "+e);
        }
        return jsonData;
    }

    private int findRegion(int regionId){
        switch (regionId){
            case 1:
                return UserEntry.REGION_CHINESE;
            case 2:
                return UserEntry.REGION_ITALIAN;
            case 3:
                return UserEntry.REGION_MEXICAN;
            case 4:
               return UserEntry.REGION_NORTH_INDIAN;
            case 5:
                return UserEntry.REGION_SOUTH_INDIAN;
            default:
                return UserEntry.REGION_DEFAULT;
        }
    }

    private int findCookingLevel(int cookingLevel){
        switch (cookingLevel){
            case 1:
                return UserEntry.LEVEL_BEGINNER;
            case 2:
                return UserEntry.LEVEL_INTERMEDIATE;
            case 3:
                return UserEntry.LEVEL_EXPERT;
            default:
                    return UserEntry.LEVEL_DEFAULT;
        }
    }

    private int findSecurityLevel(int security){
        switch (security){
            case 1:
                return RecipeEntry.SECURITY_PUBLIC;
            case 2:
                return RecipeEntry.SECURITY_MY_RECIPES;
            default:
                return RecipeEntry.SECURITY_PRIVATE;
        }
    }

    private int findDiet(int diet){
        switch (diet){
            case 1:
                return RecipeEntry.RECIPE_DIET_VEGETARIAN;
            case 2:
                return RecipeEntry.RECIPE_DIET_NON_VEGETARIAN;
            default:
                return RecipeEntry.RECIPE_DIET_VEGAN;
        }
    }

    private int findRecipeCookingLevel(int cookingLevel){
        switch (cookingLevel){
            case 1:
                return RecipeEntry.RECIPE_LEVEL_BEGINNER;
            case 2:
                return RecipeEntry.RECIPE_LEVEL_INTERMEDIATE;
            case 3:
                return RecipeEntry.RECIPE_LEVEL_EXPERT;
            default:
                return RecipeEntry.RECIPE_LEVEL_DEFAULT;
        }
    }

    private int findImageCategory(int category){
        switch (category){
            case 1:
                return ImageEntry.CATEGORY_REPORTS;
            case 2:
                return ImageEntry.CATEGORY_QUESTIONS;
            case 3:
                return ImageEntry.CATEGORY_ANSWERS;
            case 4:
                return ImageEntry.CATEGORY_COMMENTS;
            default:
                return ImageEntry.CATEGORY_RECIPE;
        }
    }

    public class loadDatabases extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences preferences= getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE);
            boolean alreadyLoaded=preferences.getBoolean(PREFS_ALREADY_LOADED,false);
            if(!alreadyLoaded){
                try{
                    JSONObject object=new JSONObject(loadJSONFromAsset());
                    JSONArray array=object.getJSONArray("users");
                    ContentValues contentValues=new ContentValues();
                    for(int i=0;i<array.length();i++){
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(UserEntry._ID,object1.getInt(UserEntry._ID));
                        contentValues.put(UserEntry.USER_ID,object1.getString(UserEntry.USER_ID));
                        contentValues.put(UserEntry.USER_NAME,object1.getString(UserEntry.USER_NAME));
                        contentValues.put(UserEntry.USER_PASSWORD,object1.getString(UserEntry.USER_PASSWORD));
                        contentValues.put(UserEntry.USER_EMAIL,object1.getString(UserEntry.USER_EMAIL));
                        contentValues.put(UserEntry.USER_REGION,findRegion(object1.getInt(UserEntry.USER_REGION)));
                        contentValues.put(UserEntry.USER_COOKING_LEVEL,findCookingLevel(object1.getInt(UserEntry.USER_COOKING_LEVEL)));
                        contentValues.put(UserEntry.USER_IMAGE,object1.getString(UserEntry.USER_IMAGE));
                        contentValues.put(UserEntry.USER_CREDITS,object1.getInt(UserEntry.USER_CREDITS));
                        getContentResolver().insert(UserEntry.CONTENT_URI,contentValues);
                    }

                    array=object.getJSONArray("recipes");
                    for(int i=0;i<array.length();i++){
                        contentValues=new ContentValues();
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(RecipeEntry._ID,object1.getInt(RecipeEntry._ID));
                        contentValues.put(RecipeEntry.RECIPE_ID,object1.getString(RecipeEntry.RECIPE_ID));
                        contentValues.put(RecipeEntry.RECIPE_BY_USER_ID,object1.getString(RecipeEntry.RECIPE_BY_USER_ID));
                        contentValues.put(RecipeEntry.RECIPE_TITLE,object1.getString(RecipeEntry.RECIPE_TITLE));
                        contentValues.put(RecipeEntry.RECIPE_SECURITY,findSecurityLevel(object1.getInt(RecipeEntry.RECIPE_SECURITY)));
                        contentValues.put(RecipeEntry.RECIPE_DESCRIPTION,object1.getString(RecipeEntry.RECIPE_DESCRIPTION));
                        contentValues.put(RecipeEntry.RECIPE_NO_OF_SERVINGS,object1.getInt(RecipeEntry.RECIPE_NO_OF_SERVINGS));
                        contentValues.put(RecipeEntry.RECIPE_COOKING_TIME,object1.getString(RecipeEntry.RECIPE_COOKING_TIME));
                        contentValues.put(RecipeEntry.RECIPE_DIET,findDiet(object1.getInt(RecipeEntry.RECIPE_DIET)));
                        contentValues.put(RecipeEntry.RECIPE_COOKING_LEVEL,findRecipeCookingLevel(object1.getInt(RecipeEntry.RECIPE_COOKING_LEVEL)));
                        contentValues.put(RecipeEntry.RECIPE_DATE,object1.getString(RecipeEntry.RECIPE_DATE));
                        contentValues.put(RecipeEntry.RECIPE_NO_OF_LIKES,object1.getInt(RecipeEntry.RECIPE_NO_OF_LIKES));
                        contentValues.put(RecipeEntry.RECIPE_EARNED_CREDITS,object1.getInt(RecipeEntry.RECIPE_EARNED_CREDITS));
                        if(object1.has(RecipeEntry.RECIPE_VIDEO)){
                            contentValues.put(RecipeEntry.RECIPE_VIDEO,object1.getString(RecipeEntry.RECIPE_VIDEO));
                        }
                        getContentResolver().insert(RecipeEntry.CONTENT_URI,contentValues);
                    }

                    array=object.getJSONArray("methods");
                    for(int i=0;i<array.length();i++){
                        contentValues=new ContentValues();
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(MethodsEntry._ID,object1.getInt(MethodsEntry._ID));
                        contentValues.put(MethodsEntry.METHODS_RECIPE_ID,object1.getString(MethodsEntry.METHODS_RECIPE_ID));
                        contentValues.put(MethodsEntry.METHODS_POSITIONS,object1.getInt(MethodsEntry.METHODS_POSITIONS));
                        if(object1.has(MethodsEntry.METHODS_DESCRIPTION)){
                            contentValues.put(MethodsEntry.METHODS_DESCRIPTION,object1.getString(MethodsEntry.METHODS_DESCRIPTION));
                        }
                        if(object1.has(MethodsEntry.METHODS_IMAGE)){
                            contentValues.put(MethodsEntry.METHODS_IMAGE,object1.getString(MethodsEntry.METHODS_IMAGE));
                        }
                        getContentResolver().insert(MethodsEntry.CONTENT_URI,contentValues);
                    }

                    array=object.getJSONArray("ingredients");
                    for(int i=0;i<array.length();i++){
                        contentValues=new ContentValues();
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(IngredientsEntry._ID,object1.getInt(IngredientsEntry._ID));
                        contentValues.put(IngredientsEntry.INGREDIENTS_RECIPE_ID,object1.getString(IngredientsEntry.INGREDIENTS_RECIPE_ID));
                        contentValues.put(IngredientsEntry.INGREDIENTS_NAME,object1.getString(IngredientsEntry.INGREDIENTS_NAME));
                        getContentResolver().insert(IngredientsEntry.CONTENT_URI,contentValues);
                    }

                    array=object.getJSONArray("images");
                    for(int i=0;i<array.length();i++){
                        contentValues=new ContentValues();
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(ImageEntry._ID,object1.getInt(ImageEntry._ID));
                        contentValues.put(ImageEntry.IMAGES_RECIPE_ID,object1.getString(ImageEntry.IMAGES_RECIPE_ID));
                        contentValues.put(ImageEntry.IMAGES_CATEGORY,findImageCategory(object1.getInt(ImageEntry.IMAGES_CATEGORY)));
                        contentValues.put(ImageEntry.IMAGES_ID,object1.getString(ImageEntry.IMAGES_ID));
                        contentValues.put(ImageEntry.IMAGES_URI,object1.getString(ImageEntry.IMAGES_URI));
                        getContentResolver().insert(ImageEntry.CONTENT_URI,contentValues);
                    }

                    array=object.getJSONArray("reference_links");
                    for(int i=0;i<array.length();i++){
                        contentValues=new ContentValues();
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(ReferenceLinkEntry._ID,object1.getInt(ReferenceLinkEntry._ID));
                        contentValues.put(ReferenceLinkEntry.LINK_RECIPE_ID,object1.getString(ReferenceLinkEntry.LINK_RECIPE_ID));
                        contentValues.put(ReferenceLinkEntry.REFERENCE_LINK,object1.getString(ReferenceLinkEntry.REFERENCE_LINK));
                        getContentResolver().insert(ReferenceLinkEntry.CONTENT_URI,contentValues);
                    }

                    array=object.getJSONArray("meal_time_category");
                    for(int i=0;i<array.length();i++){
                        contentValues=new ContentValues();
                        JSONObject object1=array.getJSONObject(i);
                        contentValues.put(MealTimeCategoryEntry._ID,object1.getInt(MealTimeCategoryEntry._ID));
                        contentValues.put(MealTimeCategoryEntry.CATEGORY_RECIPE_ID,object1.getString(MealTimeCategoryEntry.CATEGORY_RECIPE_ID));
                        contentValues.put(MealTimeCategoryEntry.MEAL_TIME_BRUNCH,object1.getInt(MealTimeCategoryEntry.MEAL_TIME_BRUNCH));
                        contentValues.put(MealTimeCategoryEntry.CATEGORY_SNACKS,object1.getInt(MealTimeCategoryEntry.CATEGORY_SNACKS));
                        contentValues.put(MealTimeCategoryEntry.CATEGORY_APPETISERS,object1.getInt(MealTimeCategoryEntry.CATEGORY_APPETISERS));
                        contentValues.put(MealTimeCategoryEntry.CATEGORY_SIDES,object1.getInt(MealTimeCategoryEntry.CATEGORY_SIDES));
                        getContentResolver().insert(MealTimeCategoryEntry.CONTENT_URI,contentValues);
                    }

                    getSharedPreferences(LoginActivity.PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putBoolean(PREFS_ALREADY_LOADED,true)
                            .apply();
                }
                catch (Exception e){
                    Log.d("me", "onCreate: "+e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
