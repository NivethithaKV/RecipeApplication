package com.example.nive_pt1681.recipeapplication.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.nive_pt1681.recipeapplication.database.RecipeContract.CalendarEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.CommentsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQAnswersEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQQuestionsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FavoritesEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FollowEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.IngredientsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.LikesEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MealTimeCategoryEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MethodsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReferenceLinkEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReportsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.TagsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;

/**
 * Created by nive-pt1681 on 22/02/18.
 */

public class RecipeProvider extends ContentProvider {

    private static RecipeHelper mRecipeHelper;
    public static final String TAG="me";
    private static final int USER_LIST_ID =1000;
    private static final int USER_ITEM_ID =1001;
    private static final int FOLLOW_LIST_ID=1002;
    private static final int FOLLOW_ITEM_ID=1003;
    private static final int RECIPE_LIST_ID=1004;
    public static final int RECIPE_ITEM_ID=1005;
    public static final int INGREDIENTS_LIST_ID=1006;
    public static final int INGREDIENTS_ITEM_ID=1007;
    public static final int METHODS_LIST_ID=1008;
    public static final int METHODS_ITEM_ID=1009;
    public static final int MEAL_TIME_CATEGORY_LIST_ID=1010;
    public static final int MEAL_TIME_CATEGORY_ITEM_ID=1011;
    public static final int CALENDAR_LIST_ID=1012;
    public static final int CALENDAR_ITEM_ID=1013;
    public static final int FAVORITES_LIST_ID=1014;
    public static final int FAVORITES_ITEM_ID=1015;
    public static final int REPORTS_LIST_ID=1016;
    public static final int REPORTS_ITEM_ID=1017;
    public static final int COMMENTS_LIST_ID=1018;
    public static final int COMMENTS_ITEM_ID=1019;
    public static final int FAQ_QUESTIONS_LIST_ID=1020;
    public static final int FAQ_QUESTIONS_ITEM_ID=1021;
    public static final int FAQ_ANSWERS_LIST_ID=1022;
    public static final int FAQ_ANSWERS_ITEM_ID=1023;
    public static final int LINKS_LIST_ID=1024;
    public static final int LINKS_ITEM_ID=1025;
    public static final int IMAGES_LIST_ID=1026;
    public static final int IMAGES_ITEM_ID=1027;
    public static final int LIKES_LIST_ID=1028;
    public static final int LIKES_ITEM_ID=1029;
    public static final int TAGS_LIST_ID=1030;
    public static final int TAGS_ITEM_ID=1031;
    public static final int NOTIFICATION_LIST_ID=1032;
    public static final int NOTIFICATION_ITEM_ID=1033;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.USERS_PATH_NAME, USER_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.USERS_PATH_NAME +"/#", USER_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FOLLOW_PATH_NAME,FOLLOW_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FOLLOW_PATH_NAME+"/#",FOLLOW_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.RECIPE_PATH_NAME,RECIPE_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.RECIPE_PATH_NAME+"/#",RECIPE_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.INGREDIENTS_PATH_NAME,INGREDIENTS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.INGREDIENTS_PATH_NAME+"/#",INGREDIENTS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.METHODS_PATH_NAME,METHODS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.METHODS_PATH_NAME+"/#",METHODS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.MEAL_TIME_AND_CATEGORY_PATH_NAME,MEAL_TIME_CATEGORY_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.MEAL_TIME_AND_CATEGORY_PATH_NAME+"/#",MEAL_TIME_CATEGORY_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.CALENDAR_PATH_NAME,CALENDAR_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.CALENDAR_PATH_NAME+"/#",CALENDAR_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FAVORITES_PATH_NAME,FAVORITES_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FAVORITES_PATH_NAME+"/#",FAVORITES_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.REPORTS_PATH_NAME,REPORTS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.REPORTS_PATH_NAME+"/#",REPORTS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.COMMENTS_PATH_NAME,COMMENTS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.COMMENTS_PATH_NAME+"/#",COMMENTS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FAQ_QUESTIONS_PATH_NAME,FAQ_QUESTIONS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FAQ_QUESTIONS_PATH_NAME+"/#",FAQ_QUESTIONS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FAQ_ANSWERS_PATH_NAME,FAQ_ANSWERS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.FAQ_ANSWERS_PATH_NAME+"/#",FAQ_ANSWERS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.REFERENCE_LINKS_PATH_NAME,LINKS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.REFERENCE_LINKS_PATH_NAME+"/#",LINKS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.IMAGES_PATH_NAME,IMAGES_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.IMAGES_PATH_NAME+"/#",IMAGES_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.LIKES_PATH_NAME,LIKES_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.LIKES_PATH_NAME+"/#",LIKES_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.TAGS_PATH_NAME,TAGS_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.TAGS_PATH_NAME+"/#",TAGS_ITEM_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.NOTIFICATION_PATH_NAME,NOTIFICATION_LIST_ID);
        sUriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY,RecipeContract.NOTIFICATION_PATH_NAME+"/#",NOTIFICATION_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        mRecipeHelper=new RecipeHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase database=mRecipeHelper.getReadableDatabase();
        Cursor cursor=null;
        int matcher=sUriMatcher.match(uri);
        String TABLE_NAME=matchURI(matcher);
        Log.d(TAG, "query: "+TABLE_NAME);
        if(TABLE_NAME!=null){
            cursor=database.query(TABLE_NAME,strings,s,strings1,null,null,s1);
            Log.d(TAG, "query: count "+cursor.getCount());
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case USER_LIST_ID:
                return UserEntry.USER_CONTENT_LIST_URI;
            case USER_ITEM_ID:
                return UserEntry.USER_CONTENT_ITEM_URI;
            case FOLLOW_LIST_ID:
                return FollowEntry.FOLLOW_CONTENT_LIST_URI;
            case FOLLOW_ITEM_ID:
                return FollowEntry.FOLLOW_CONTENT_ITEM_URI;
            case RECIPE_LIST_ID:
                return RecipeEntry.RECIPE_CONTENT_LIST_URI;
            case RECIPE_ITEM_ID:
                return RecipeEntry.RECIPE_CONTENT_ITEM_URI;
            case INGREDIENTS_LIST_ID:
                return IngredientsEntry.INGREDIENTS_CONTENT_LIST_URI;
            case INGREDIENTS_ITEM_ID:
                return IngredientsEntry.INGREDIENTS_CONTENT_ITEM_URI;
            case METHODS_LIST_ID:
                return MethodsEntry.METHODS_CONTENT_LIST_URI;
            case METHODS_ITEM_ID:
                return MethodsEntry.METHODS_CONTENT_ITEM_URI;
            case MEAL_TIME_CATEGORY_LIST_ID:
                return MealTimeCategoryEntry.CATEGORY_CONTENT_LIST_URI;
            case MEAL_TIME_CATEGORY_ITEM_ID:
                return MealTimeCategoryEntry.CATEGORY_CONTENT_ITEM_URI;
            case CALENDAR_LIST_ID:
                return CalendarEntry.CALENDAR_CONTENT_LIST_URI;
            case CALENDAR_ITEM_ID:
                return CalendarEntry.CALENDAR_CONTENT_ITEM_URI;
            case FAVORITES_LIST_ID:
                return FavoritesEntry.FAVORITES_CONTENT_LIST_URI;
            case FAVORITES_ITEM_ID:
                return FavoritesEntry.FAVORITES_CONTENT_ITEM_URI;
            case REPORTS_LIST_ID:
                return ReportsEntry.REPORTS_CONTENT_LIST_URI;
            case REPORTS_ITEM_ID:
                return ReportsEntry.REPORTS_CONTENT_ITEM_URI;
            case COMMENTS_LIST_ID:
                return CommentsEntry.COMMENTS_CONTENT_LIST_URI;
            case COMMENTS_ITEM_ID:
                return CommentsEntry.COMMENTS_CONTENT_ITEM_URI;
            case FAQ_QUESTIONS_LIST_ID:
                return FAQQuestionsEntry.QUESTIONS_CONTENT_LIST_URI;
            case FAQ_QUESTIONS_ITEM_ID:
                return FAQQuestionsEntry.QUESTIONS_CONTENT_ITEM_URI;
            case FAQ_ANSWERS_LIST_ID:
                return FAQAnswersEntry.ANSWERS_CONTENT_LIST_URI;
            case FAQ_ANSWERS_ITEM_ID:
                return FAQAnswersEntry.ANSWERS_CONTENT_ITEM_URI;
            case LINKS_LIST_ID:
                return ReferenceLinkEntry.LINK_CONTENT_LIST_URI;
            case LINKS_ITEM_ID:
                return ReferenceLinkEntry.LINK_CONTENT_ITEM_URI;
            case IMAGES_LIST_ID:
                return ImageEntry.IMAGES_CONTENT_LIST_URI;
            case IMAGES_ITEM_ID:
                return ImageEntry.IMAGES_CONTENT_ITEM_URI;
            case LIKES_LIST_ID:
                return LikesEntry.LIKES_CONTENT_LIST_URI;
            case LIKES_ITEM_ID:
                return LikesEntry.LIKES_CONTENT_ITEM_URI;
            case TAGS_LIST_ID:
                return TagsEntry.TAGS_CONTENT_LIST_URI;
            case TAGS_ITEM_ID:
                return TagsEntry.TAGS_CONTENT_ITEM_URI;
            case NOTIFICATION_LIST_ID:
                return NotificationEntry.NOTIFICATION_CONTENT_LIST_URI;
            case NOTIFICATION_ITEM_ID:
                return NotificationEntry.NOTIFICATION_CONTENT_ITEM_URI;
        }
        return "Unknown URI";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match=sUriMatcher.match(uri);
        long id=-1;
        String TABLE_NAME=matchURI(match);
        SQLiteDatabase database=mRecipeHelper.getWritableDatabase();
        id=database.insert(TABLE_NAME,null,contentValues);
        if(id==-1)
            return null;
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match=sUriMatcher.match(uri);
        int rowsDeleted=0;
        String TABLE_NAME=matchURI(match);
        SQLiteDatabase database=mRecipeHelper.getWritableDatabase();
        rowsDeleted=database.delete(TABLE_NAME,s,strings);
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int match=sUriMatcher.match(uri);
        int rowsAffected=0;
        SQLiteDatabase database=mRecipeHelper.getWritableDatabase();
        String TABLE_NAME=matchURI(match);
        rowsAffected=database.update(TABLE_NAME,contentValues,s,strings);
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsAffected;
    }

    public String matchURI(int matcher){
        String TABLE_NAME=null;
        switch (matcher){
            case USER_LIST_ID:
            case USER_ITEM_ID:
                TABLE_NAME=UserEntry.TABLE_NAME;
                break;
            case FOLLOW_LIST_ID:
            case FOLLOW_ITEM_ID:
                TABLE_NAME=FollowEntry.TABLE_NAME;
                break;
            case RECIPE_LIST_ID:
            case RECIPE_ITEM_ID:
                TABLE_NAME= RecipeEntry.TABLE_NAME;
                break;
            case INGREDIENTS_LIST_ID:
            case INGREDIENTS_ITEM_ID:
                TABLE_NAME= IngredientsEntry.TABLE_NAME;
                break;
            case METHODS_LIST_ID:
            case METHODS_ITEM_ID:
                TABLE_NAME= MethodsEntry.TABLE_NAME;
                break;
            case MEAL_TIME_CATEGORY_LIST_ID:
            case MEAL_TIME_CATEGORY_ITEM_ID:
                TABLE_NAME= MealTimeCategoryEntry.TABLE_NAME;
                break;
            case CALENDAR_LIST_ID:
            case CALENDAR_ITEM_ID:
                TABLE_NAME= CalendarEntry.TABLE_NAME;
                break;
            case FAVORITES_LIST_ID:
            case FAVORITES_ITEM_ID:
                TABLE_NAME= FavoritesEntry.TABLE_NAME;
                break;
            case REPORTS_LIST_ID:
            case REPORTS_ITEM_ID:
                TABLE_NAME= ReportsEntry.TABLE_NAME;
                break;
            case COMMENTS_LIST_ID:
            case COMMENTS_ITEM_ID:
                TABLE_NAME= CommentsEntry.TABLE_NAME;
                break;
            case FAQ_QUESTIONS_LIST_ID:
            case FAQ_QUESTIONS_ITEM_ID:
                TABLE_NAME= FAQQuestionsEntry.TABLE_NAME;
                break;
            case FAQ_ANSWERS_LIST_ID:
            case FAQ_ANSWERS_ITEM_ID:
                TABLE_NAME= FAQAnswersEntry.TABLE_NAME;
                break;
            case LINKS_LIST_ID:
            case LINKS_ITEM_ID:
                TABLE_NAME= ReferenceLinkEntry.TABLE_NAME;
                break;
            case IMAGES_LIST_ID:
            case IMAGES_ITEM_ID:
                TABLE_NAME= ImageEntry.TABLE_NAME;
                break;
            case LIKES_LIST_ID:
            case LIKES_ITEM_ID:
                TABLE_NAME= LikesEntry.TABLE_NAME;
                break;
            case TAGS_LIST_ID:
            case TAGS_ITEM_ID:
                TABLE_NAME= TagsEntry.TABLE_NAME;
                break;
            case NOTIFICATION_LIST_ID:
            case NOTIFICATION_ITEM_ID:
                TABLE_NAME= NotificationEntry.TABLE_NAME;
                break;
        }
        return TABLE_NAME;
    }
}
