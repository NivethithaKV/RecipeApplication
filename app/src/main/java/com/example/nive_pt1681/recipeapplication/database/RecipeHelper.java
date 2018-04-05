package com.example.nive_pt1681.recipeapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

public class RecipeHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="cookbook.db";
    private static final int VERSION=1;

    private static final String CREATE_USER_TABLE ="CREATE TABLE "+ UserEntry.TABLE_NAME
            + "(" +UserEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +UserEntry.USER_ID+" TEXT PRIMARY KEY, "
            +UserEntry.USER_NAME+" TEXT NOT NULL, "
            +UserEntry.USER_EMAIL+" TEXT NOT NULL UNIQUE, "
            +UserEntry.USER_COOKING_LEVEL+" INTEGER NOT NULL, "
            +UserEntry.USER_PASSWORD+" TEXT NOT NULL, "
            +UserEntry.USER_REGION+" INTEGER NOT NULL, "
            +UserEntry.USER_IMAGE+" TEXT, "
            +UserEntry.USER_CREDITS+" INTEGER NOT NULL)";


    private static final String CREATE_FOLLOW_TABLE ="CREATE TABLE "+ FollowEntry.TABLE_NAME
            +"("+FollowEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +FollowEntry.FOLLOWER+ " TEXT NOT NULL, "
            +FollowEntry.FOLLOWING +" TEXT NOT NULL, "
            +"FOREIGN KEY ("+ FollowEntry.FOLLOWER + ") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            +"FOREIGN KEY ("+FollowEntry.FOLLOWING +") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"))";


    public static final String CREATE_RECIPE_TABLE="CREATE TABLE "+ RecipeEntry.TABLE_NAME
            +"("+RecipeEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +RecipeEntry.RECIPE_ID+" TEXT PRIMARY KEY, "
            +RecipeEntry.RECIPE_BY_USER_ID+" TEXT NOT NULL, "
            +RecipeEntry.RECIPE_TITLE+" TEXT NOT NULL, "
            + RecipeEntry.RECIPE_DESCRIPTION+" TEXT NOT NULL, "
            +RecipeEntry.RECIPE_SECURITY+" TEXT NOT NULL, "
            +RecipeEntry.RECIPE_NO_OF_SERVINGS+" INTEGER NOT NULL, "
            +RecipeEntry.RECIPE_COOKING_TIME+" TEXT NOT NULL, "
            +RecipeEntry.RECIPE_DIET+" INTEGER NOT NULL, "
            +RecipeEntry.RECIPE_COOKING_LEVEL+" INTEGER NOT NULL, "
            +RecipeEntry.RECIPE_DATE+" TEXT NOT NULL, "
            +RecipeEntry.RECIPE_NO_OF_LIKES+" INTEGER NOT NULL, "
            +RecipeEntry.RECIPE_EARNED_CREDITS+" INTEGER NOT NULL, "
            +RecipeEntry.RECIPE_VIDEO+" TEXT, "
            +"FOREIGN KEY ("+RecipeEntry.RECIPE_BY_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+ UserEntry.USER_ID+"))";


    public static final String CREATE_CALENDAR_TABLE="CREATE TABLE "+ CalendarEntry.TABLE_NAME
            +"("+CalendarEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +CalendarEntry.CALENDAR_ID+" TEXT PRIMARY KEY, "
            +CalendarEntry.CALENDAR_USER_ID+" TEXT NOT NULL, "
            + CalendarEntry.CALENDAR_RECIPE_ID+" TEXT NOT NULL, "
            + CalendarEntry.CALENDAR_DATE+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+CalendarEntry.CALENDAR_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+ UserEntry.USER_ID+"), "
            +"FOREIGN KEY ("+CalendarEntry.CALENDAR_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";


    public static final String CREATE_FAVORITES_TABLE="CREATE TABLE "+ FavoritesEntry.TABLE_NAME
            +"("+FavoritesEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +FavoritesEntry.FAVORITES_USER_ID+" TEXT NOT NULL, "
            +FavoritesEntry.FAVORITES_RECIPE_ID+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+FavoritesEntry.FAVORITES_USER_ID+") REFERENCES " +UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            + "FOREIGN KEY ("+FavoritesEntry.FAVORITES_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"(" +RecipeEntry.RECIPE_ID+"))";

    public static final String CREATE_LIKES_TABLE="CREATE TABLE "+ LikesEntry.TABLE_NAME
            +"("+LikesEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +LikesEntry.LIKES_USER_ID+" TEXT NOT NULL, "
            +LikesEntry.LIKES_RECIPE_ID+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+LikesEntry.LIKES_USER_ID+") REFERENCES " +UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            + "FOREIGN KEY ("+LikesEntry.LIKES_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"(" +RecipeEntry.RECIPE_ID+"))";

    public static final String CREATE_TAGS_TABLE="CREATE TABLE "+ TagsEntry.TABLE_NAME
            +"("+TagsEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +TagsEntry.TAGS_USER_ID+" TEXT NOT NULL, "
            +TagsEntry.TAGS_RECIPE_ID+" TEXT NOT NULL, "
            +TagsEntry.TAGS_TAG+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+TagsEntry.TAGS_USER_ID+") REFERENCES " +UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            + "FOREIGN KEY ("+TagsEntry.TAGS_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"(" +RecipeEntry.RECIPE_ID+"))";


    public static final String CREATE_REPORTS_TABLE="CREATE TABLE "+ ReportsEntry.TABLE_NAME
            +"("+ReportsEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +ReportsEntry.REPORTS_ID+" TEXT PRIMARY KEY, "
            +ReportsEntry.REPORTS_USER_ID+" TEXT NOT NULL, "
            +ReportsEntry.REPORTS_RECIPE_ID+" TEXT NOT NULL, "
            +ReportsEntry.REPORTS_REGARDING+" TEXT NOT NULL, "
            +ReportsEntry.REPORTS_PROBLEM+" TEXT NOT NULL, "
            +ReportsEntry.REPORTS_VULNERABILITY+" TEXT NOT NULL, "
            +ReportsEntry.REPORTS_DATE+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+ReportsEntry.REPORTS_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            +"FOREIGN KEY ("+ReportsEntry.REPORTS_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";


    public static final String CREATE_COMMENTS_TABLE="CREATE TABLE "+ CommentsEntry.TABLE_NAME
            +"("+CommentsEntry._ID +" INTEGER NOT NULL UNIQUE, "
            +CommentsEntry.COMMENTS_ID+" TEXT PRIMARY KEY, "
            +CommentsEntry.COMMENTS_USER_ID+" TEXT NOT NULL, "
            +CommentsEntry.COMMENTS_RECIPE_ID+" TEXT NOT NULL, "
            +CommentsEntry.COMMENTS_COMMENT+" TEXT NOT NULL, "
            +CommentsEntry.COMMENTS_DATE+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+CommentsEntry.COMMENTS_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            +"FOREIGN KEY ("+CommentsEntry.COMMENTS_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";


    public static final String CREATE_FAQ_QUESTIONS_TABLE="CREATE TABLE "+ FAQQuestionsEntry.TABLE_NAME
            +"("+FAQQuestionsEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +FAQQuestionsEntry.QUESTIONS_ID+" TEXT PRIMARY KEY, "
            +FAQQuestionsEntry.QUESTIONS_USER_ID+" TEXT NOT NULL, "
            +FAQQuestionsEntry.QUESTIONS_QUESTION+" TEXT NOT NULL, "
            +FAQQuestionsEntry.QUESTIONS_DATE+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+FAQQuestionsEntry.QUESTIONS_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"))";

    public static final String CREATE_FAQ_ANSWERS_TABLE="CREATE TABLE "+ FAQAnswersEntry.TABLE_NAME
            +"("+FAQAnswersEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +FAQAnswersEntry.ANSWERS_ID+" TEXT PRIMARY KEY, "
            +FAQAnswersEntry.ANSWERS_QUESTION_ID+" TEXT NOT NULL, "
            +FAQAnswersEntry.ANSWERS_USER_ID+" TEXT NOT NULL, "
            +FAQAnswersEntry.ANSWERS_ANSWER+" TEXT NOT NULL, "
            +FAQAnswersEntry.ANSWERS_DATE+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+FAQAnswersEntry.ANSWERS_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            +"FOREIGN KEY ("+FAQAnswersEntry.ANSWERS_QUESTION_ID+") REFERENCES "+FAQQuestionsEntry.TABLE_NAME+"("+FAQQuestionsEntry.QUESTIONS_ID+"))";

    public static final String CREATE_INGREDIENTS_TABLE="CREATE TABLE "+ IngredientsEntry.TABLE_NAME
            +"("+IngredientsEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +IngredientsEntry.INGREDIENTS_RECIPE_ID+" TEXT NOT NULL, "
            +IngredientsEntry.INGREDIENTS_NAME+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+IngredientsEntry.INGREDIENTS_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";

    public static final String CREATE_METHODS_TABLE="CREATE TABLE "+ MethodsEntry.TABLE_NAME
            +"("+ MethodsEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +MethodsEntry.METHODS_RECIPE_ID+" TEXT NOT NULL, "
            +MethodsEntry.METHODS_POSITIONS+" INTEGER NOT NULL, "
            +MethodsEntry.METHODS_DESCRIPTION +" TEXT, "
            +MethodsEntry.METHODS_IMAGE+" TEXT, "
            +"FOREIGN KEY ("+MethodsEntry.METHODS_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";

    public static final String CREATE_IMAGES_TABLE="CREATE TABLE "+ ImageEntry.TABLE_NAME
            +"("+ImageEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +ImageEntry.IMAGES_ID+" TEXT PRIMARY KEY, "
            +ImageEntry.IMAGES_RECIPE_ID+" TEXT, "
            +ImageEntry.IMAGES_CATEGORY +" INTEGER NOT NULL, "
            +ImageEntry.IMAGES_URI+" TEXT NOT NULL, "
            +ImageEntry.IMAGES_REPORT_ID+" TEXT, "
            +ImageEntry.IMAGES_COMMENT_ID+" TEXT, "
            +ImageEntry.IMAGES_ANSWER_ID+" TEXT, "
            +ImageEntry.IMAGES_QUESTION+" TEXT, "
            +"FOREIGN KEY ("+ImageEntry.IMAGES_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"), "
            +"FOREIGN KEY ("+ImageEntry.IMAGES_REPORT_ID+") REFERENCES "+ReportsEntry.TABLE_NAME+"("+ReportsEntry.REPORTS_ID+"), "
            +"FOREIGN KEY ("+ImageEntry.IMAGES_COMMENT_ID+") REFERENCES "+CommentsEntry.TABLE_NAME+"("+CommentsEntry.COMMENTS_ID+"), "
            +"FOREIGN KEY ("+ImageEntry.IMAGES_QUESTION+") REFERENCES "+FAQQuestionsEntry.TABLE_NAME+"("+FAQQuestionsEntry.QUESTIONS_ID+"), "
            +"FOREIGN KEY ("+ImageEntry.IMAGES_ANSWER_ID+") REFERENCES "+FAQAnswersEntry.TABLE_NAME+"("+FAQAnswersEntry.ANSWERS_ID+"))";

    public static final String CREATE_LINKS_TABLE="CREATE TABLE "+ ReferenceLinkEntry.TABLE_NAME
            +"("+ReferenceLinkEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +ReferenceLinkEntry.REFERENCE_LINK+" TEXT NOT NULL, "
            +ReferenceLinkEntry.LINK_RECIPE_ID+" TEXT NOT NULL, "
            +"FOREIGN KEY ("+ReferenceLinkEntry.LINK_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";

    public static final String CREATE_MEAL_TIME_CATEGORY_TABLE="CREATE TABLE "+ MealTimeCategoryEntry.TABLE_NAME
            +"("+MealTimeCategoryEntry._ID+" INTEGER NOT NULL UNIQUE, "
            +MealTimeCategoryEntry.CATEGORY_RECIPE_ID+" TEXT NOT NULL, "
            +MealTimeCategoryEntry.MEAL_TIME_BREAKFAST+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.MEAL_TIME_BRUNCH+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.MEAL_TIME_LUNCH+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.MEAL_TIME_DINNER+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_APPETISERS +" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_BAKING+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_BURGER+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_CAKES+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_DESSERTS+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_DRINKS+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_GRAVY+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_MEAT+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_NOODLES+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_PASTA+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_PIZZA+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_POULTRY+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_RICE+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_SALADS+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_SEAFOOD+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_SIDES+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_SAUCES+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_SNACKS+" INTEGER NOT NULL DEFAULT 0, "
            +MealTimeCategoryEntry.CATEGORY_SOUPS+" INTEGER NOT NULL DEFAULT 0, "
            +"FOREIGN KEY ("+MealTimeCategoryEntry.CATEGORY_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";

    public static final String CREATE_NOTIFICATION_TABLE="CREATE TABLE "+ NotificationEntry.TABLE_NAME
            +"("+NotificationEntry._ID +" INTEGER NOT NULL UNIQUE, "
            +NotificationEntry.NOTIFICATION_ID+" TEXT PRIMARY KEY, "
            +NotificationEntry.NOTIFICATION_TO_USER_ID+" TEXT NOT NULL, "
            +NotificationEntry.NOTIFICATION_MESSAGE+" TEXT NOT NULL, "
            +NotificationEntry.NOTIFICATION_RECIPE_ID+" TEXT, "
            +NotificationEntry.NOTIFICATION_CATEGORY+" INTEGER NOT NULL, "
            +"FOREIGN KEY ("+NotificationEntry.NOTIFICATION_TO_USER_ID+") REFERENCES "+UserEntry.TABLE_NAME+"("+UserEntry.USER_ID+"), "
            +"FOREIGN KEY ("+NotificationEntry.NOTIFICATION_RECIPE_ID+") REFERENCES "+RecipeEntry.TABLE_NAME+"("+RecipeEntry.RECIPE_ID+"))";

    public RecipeHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_FOLLOW_TABLE);
        sqLiteDatabase.execSQL(CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(CREATE_INGREDIENTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_METHODS_TABLE);
        sqLiteDatabase.execSQL(CREATE_MEAL_TIME_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_CALENDAR_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVORITES_TABLE);
        sqLiteDatabase.execSQL(CREATE_REPORTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_COMMENTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAQ_QUESTIONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAQ_ANSWERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_LINKS_TABLE);
        sqLiteDatabase.execSQL(CREATE_IMAGES_TABLE);
        sqLiteDatabase.execSQL(CREATE_LIKES_TABLE);
        sqLiteDatabase.execSQL(CREATE_TAGS_TABLE);
        sqLiteDatabase.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
