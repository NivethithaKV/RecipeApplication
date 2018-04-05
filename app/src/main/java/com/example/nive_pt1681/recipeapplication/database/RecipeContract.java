package com.example.nive_pt1681.recipeapplication.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nive-pt1681 on 22/02/18.
 */

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.nive_pt1681.recipeapplication";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String USERS_PATH_NAME = "users";
    public static final String FOLLOW_PATH_NAME = "follow";
    public static final String RECIPE_PATH_NAME = "recipes";
    public static final String CALENDAR_PATH_NAME = "add_to_calendar";
    public static final String FAVORITES_PATH_NAME = "favorites";
    public static final String REPORTS_PATH_NAME = "reports";
    public static final String COMMENTS_PATH_NAME = "comments";
    public static final String FAQ_QUESTIONS_PATH_NAME = "faq_questions";
    public static final String FAQ_ANSWERS_PATH_NAME = "faq_answers";
    public static final String REFERENCE_LINKS_PATH_NAME = "reference_links";
    public static final String INGREDIENTS_PATH_NAME = "ingredients";
    public static final String METHODS_PATH_NAME = "methods";
    public static final String IMAGES_PATH_NAME = "image";
    public static final String MEAL_TIME_AND_CATEGORY_PATH_NAME = "meal_time_category";
    public static final String LIKES_PATH_NAME="likes";
    public static final String TAGS_PATH_NAME="tags";
    public static final String NOTIFICATION_PATH_NAME="notifications";

    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, USERS_PATH_NAME);

        public static final String TABLE_NAME = "users";

        public static final String _ID = BaseColumns._ID;
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "name";
        public static final String USER_PASSWORD = "password";
        public static final String USER_EMAIL = "email";
        public static final String USER_REGION = "region";
        public static final String USER_COOKING_LEVEL = "cooking_level";
        public static final String USER_IMAGE = "user_image";
        public static final String USER_CREDITS = "user_credits";

        public static final int REGION_DEFAULT = 0;
        public static final int REGION_CHINESE = 1;
        public static final int REGION_ITALIAN = 2;
        public static final int REGION_MEXICAN = 3;
        public static final int REGION_NORTH_INDIAN = 4;
        public static final int REGION_SOUTH_INDIAN = 5;

        public static final int LEVEL_DEFAULT = 0;
        public static final int LEVEL_BEGINNER = 1;
        public static final int LEVEL_INTERMEDIATE = 2;
        public static final int LEVEL_EXPERT = 3;

        public static final String USER_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + USERS_PATH_NAME;
        public static final String USER_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + USERS_PATH_NAME;
    }


    public static final class FollowEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, FOLLOW_PATH_NAME);

        public static final String TABLE_NAME = "follow";

        public static final String _ID = BaseColumns._ID;
        public static final String FOLLOWER = "follower";
        public static final String FOLLOWING = "following";

        public static final String FOLLOW_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FOLLOW_PATH_NAME;
        public static final String FOLLOW_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FOLLOW_PATH_NAME;
    }


    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, RECIPE_PATH_NAME);

        public static final String TABLE_NAME = "recipes";

        public static final String _ID = BaseColumns._ID;
        public static final String RECIPE_ID = "recipe_id";
        public static final String RECIPE_BY_USER_ID = "recipe_by_user_id";
        public static final String RECIPE_TITLE = "recipe_title";
        public static final String RECIPE_SECURITY = "recipe_security";
        public static final String RECIPE_DESCRIPTION = "recipe_description";
        public static final String RECIPE_NO_OF_SERVINGS = "recipe_no_of_servings";
        public static final String RECIPE_COOKING_TIME = "recipe_cooking_time";
        public static final String RECIPE_DIET = "recipe_diet";
        public static final String RECIPE_COOKING_LEVEL = "recipe_cooking_level";
        public static final String RECIPE_DATE = "recipe_date";
        public static final String RECIPE_NO_OF_LIKES="recipe_no_of_likes";
        public static final String RECIPE_EARNED_CREDITS = "recipe_earned_credits";
        public static final String RECIPE_VIDEO="recipe_video";

        public static final int SECURITY_PRIVATE=0;
        public static final int SECURITY_PUBLIC=1;
        public static final int SECURITY_MY_RECIPES=2;

        public static final int RECIPE_DIET_VEGAN = 0;
        public static final int RECIPE_DIET_VEGETARIAN = 1;
        public static final int RECIPE_DIET_NON_VEGETARIAN = 2;

        public static final int RECIPE_LEVEL_DEFAULT = 0;
        public static final int RECIPE_LEVEL_BEGINNER = 1;
        public static final int RECIPE_LEVEL_INTERMEDIATE = 2;
        public static final int RECIPE_LEVEL_EXPERT = 3;

        public static final String RECIPE_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + RECIPE_PATH_NAME;
        public static final String RECIPE_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + RECIPE_PATH_NAME;
    }


    public static final class CalendarEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CALENDAR_PATH_NAME);

        public static final String TABLE_NAME = "calendar";

        public static final String _ID = BaseColumns._ID;
        public static final String CALENDAR_ID = "calendar_id";
        public static final String CALENDAR_USER_ID = "calendar_user_id";
        public static final String CALENDAR_RECIPE_ID = "calendar_recipe_id";
        public static final String CALENDAR_DATE = "calendar_date";

        public static final String CALENDAR_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CALENDAR_PATH_NAME;
        public static final String CALENDAR_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CALENDAR_PATH_NAME;
    }


    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, FAVORITES_PATH_NAME);

        public static final String TABLE_NAME = "favorites";

        public static final String _ID = BaseColumns._ID;
        public static final String FAVORITES_USER_ID = "favorites_user_id";
        public static final String FAVORITES_RECIPE_ID="favorites_recipe_id";

        public static final String FAVORITES_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAVORITES_PATH_NAME;
        public static final String FAVORITES_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAVORITES_PATH_NAME;
    }

    public static final class LikesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, LIKES_PATH_NAME);

        public static final String TABLE_NAME = "likes";

        public static final String _ID = BaseColumns._ID;
        public static final String LIKES_USER_ID = "likes_user_id";
        public static final String LIKES_RECIPE_ID="likes_recipe_id";

        public static final String LIKES_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + LIKES_PATH_NAME;
        public static final String LIKES_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + LIKES_PATH_NAME;
    }

    public static final class TagsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TAGS_PATH_NAME);

        public static final String TABLE_NAME = "tags";

        public static final String _ID = BaseColumns._ID;
        public static final String TAGS_USER_ID = "tags_user_id";
        public static final String TAGS_RECIPE_ID="tags_recipe_id";
        public static final String TAGS_TAG="tags_tag";

        public static final String TAGS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TAGS_PATH_NAME;
        public static final String TAGS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TAGS_PATH_NAME;
    }


    public static final class ReportsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, REPORTS_PATH_NAME);

        public static final String TABLE_NAME = "reports";

        public static final String _ID = BaseColumns._ID;
        public static final String REPORTS_ID = "reports_id";
        public static final String REPORTS_USER_ID = "reports_user_id";
        public static final String REPORTS_RECIPE_ID = "reports_recipe_id";
        public static final String REPORTS_REGARDING = "reports_regarding";
        public static final String REPORTS_PROBLEM = "reports_problem";
        public static final String REPORTS_VULNERABILITY = "reports_vulnerability";
        public static final String REPORTS_DATE = "reports_date";

        public static final String REPORTS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + REPORTS_PATH_NAME;
        public static final String REPORTS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + REPORTS_PATH_NAME;
    }


    public static final class CommentsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, COMMENTS_PATH_NAME);

        public static final String TABLE_NAME = "comments";

        public static final String _ID = BaseColumns._ID;
        public static final String COMMENTS_ID = "comments_id";
        public static final String COMMENTS_USER_ID = "comments_user_id";
        public static final String COMMENTS_RECIPE_ID = "comments_recipe_id";
        public static final String COMMENTS_COMMENT = "comments_comment";
        public static final String COMMENTS_DATE = "comments_date";

        public static final String COMMENTS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + COMMENTS_PATH_NAME;
        public static final String COMMENTS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + COMMENTS_PATH_NAME;
    }


    public static final class FAQQuestionsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, FAQ_QUESTIONS_PATH_NAME);

        public static final String TABLE_NAME = "faq_questions";

        public static final String _ID = BaseColumns._ID;
        public static final String QUESTIONS_ID = "questions_id";
        public static final String QUESTIONS_USER_ID = "questions_user_id";
        public static final String QUESTIONS_QUESTION = "questions_question";
        public static final String QUESTIONS_DATE = "questions_date";

        public static final String QUESTIONS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAQ_QUESTIONS_PATH_NAME;
        public static final String QUESTIONS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAQ_QUESTIONS_PATH_NAME;
    }


    public static final class FAQAnswersEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, FAQ_ANSWERS_PATH_NAME);

        public static final String TABLE_NAME = "faq_answers";

        public static final String _ID = BaseColumns._ID;
        public static final String ANSWERS_ID = "answers_id";
        public static final String ANSWERS_QUESTION_ID = "answers_questions_id";
        public static final String ANSWERS_USER_ID = "answers_user_id";
        public static final String ANSWERS_ANSWER = "answers_answer";
        public static final String ANSWERS_DATE = "answers_date";

        public static final String ANSWERS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAQ_ANSWERS_PATH_NAME;
        public static final String ANSWERS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + FAQ_ANSWERS_PATH_NAME;
    }


    public static final class IngredientsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, INGREDIENTS_PATH_NAME);

        public static final String TABLE_NAME = "ingredients";

        public static final String _ID = BaseColumns._ID;
        public static final String INGREDIENTS_NAME= "ingredients_name";
        public static final String INGREDIENTS_RECIPE_ID = "ingredients_recipe_id";

        public static final String INGREDIENTS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + INGREDIENTS_PATH_NAME;
        public static final String INGREDIENTS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + INGREDIENTS_PATH_NAME;
    }


    public static final class MethodsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, METHODS_PATH_NAME);

        public static final String TABLE_NAME = "methods";

        public static final String _ID = BaseColumns._ID;
        public static final String METHODS_RECIPE_ID = "methods_recipe_id";
        public static final String METHODS_POSITIONS = "methods_positions";
        public static final String METHODS_DESCRIPTION = "methods_description";
        public static final String METHODS_IMAGE = "methods_image";

        public static final String METHODS_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + METHODS_PATH_NAME;
        public static final String METHODS_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + METHODS_PATH_NAME;
    }


    public static final class ImageEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, IMAGES_PATH_NAME);

        public static final String TABLE_NAME = "images";

        public static final String _ID = BaseColumns._ID;
        public static final String IMAGES_ID = "images_id";
        public static final String IMAGES_RECIPE_ID = "images_recipe_id";
        public static final String IMAGES_CATEGORY = "images_category";
        public static final String IMAGES_URI = "images_uri";
        public static final String IMAGES_REPORT_ID = "images_report_id";
        public static final String IMAGES_COMMENT_ID= "images_comment_id";
        public static final String IMAGES_QUESTION = "images_question_id";
        public static final String IMAGES_ANSWER_ID = "images_answer_id";

        public static final int CATEGORY_RECIPE=0;
        public static final int CATEGORY_REPORTS=1;
        public static final int CATEGORY_QUESTIONS=2;
        public static final int CATEGORY_ANSWERS=3;
        public static final int CATEGORY_COMMENTS=4;

        public static final String IMAGES_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + IMAGES_PATH_NAME;
        public static final String IMAGES_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + IMAGES_PATH_NAME;
    }


    public static final class ReferenceLinkEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, REFERENCE_LINKS_PATH_NAME);

        public static final String TABLE_NAME = "reference_links";

        public static final String _ID = BaseColumns._ID;
        public static final String REFERENCE_LINK = "reference_link";
        public static final String LINK_RECIPE_ID = "link_recipe_id";

        public static final String LINK_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + REFERENCE_LINKS_PATH_NAME;
        public static final String LINK_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + REFERENCE_LINKS_PATH_NAME;
    }


    public static final class MealTimeCategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, MEAL_TIME_AND_CATEGORY_PATH_NAME);

        public static final String TABLE_NAME = "meal_time_category";

        public static final String _ID = BaseColumns._ID;
        public static final String CATEGORY_RECIPE_ID = "category_recipe_id";
        public static final String MEAL_TIME_BREAKFAST = "meal_time_breakfast";
        public static final String MEAL_TIME_BRUNCH = "meal_time_brunch";
        public static final String MEAL_TIME_LUNCH = "meal_time_lunch";
        public static final String MEAL_TIME_DINNER= "meal_time_dinner";
        public static final String CATEGORY_SNACKS = "category_snacks";
        public static final String CATEGORY_APPETISERS = "category_appetisers";
        public static final String CATEGORY_SOUPS = "category_soups";
        public static final String CATEGORY_NOODLES = "category_noodles";
        public static final String CATEGORY_RICE = "category_rice";
        public static final String CATEGORY_PASTA = "category_pasta";
        public static final String CATEGORY_MEAT = "category_meat";
        public static final String CATEGORY_POULTRY = "category_poultry";
        public static final String CATEGORY_SEAFOOD = "category_seafood";
        public static final String CATEGORY_SALADS = "category_salads";
        public static final String CATEGORY_SIDES = "category_sides";
        public static final String CATEGORY_SAUCES = "category_sauces";
        public static final String CATEGORY_BAKING = "category_baking";
        public static final String CATEGORY_DESSERTS = "category_desserts";
        public static final String CATEGORY_DRINKS = "category_drinks";
        public static final String CATEGORY_CAKES = "category_cakes";
        public static final String CATEGORY_BURGER = "category_burger";
        public static final String CATEGORY_PIZZA = "category_pizza";
        public static final String CATEGORY_GRAVY = "category_gravy";

        public static final String CATEGORY_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MEAL_TIME_AND_CATEGORY_PATH_NAME;
        public static final String CATEGORY_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MEAL_TIME_AND_CATEGORY_PATH_NAME;
    }

    public static final class NotificationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NOTIFICATION_PATH_NAME);

        public static final String TABLE_NAME = "notifications";

        public static final String _ID = BaseColumns._ID;
        public static final String NOTIFICATION_ID = "notification_id";
        public static final String NOTIFICATION_TO_USER_ID = "notification_to_user_id";
        public static final String NOTIFICATION_MESSAGE="notification_message";
        public static final String NOTIFICATION_CATEGORY="notification_category";
        public static final String NOTIFICATION_RECIPE_ID="notification_recipe_id";

        public static final int CATEGORY_FOLLOW=0;
        public static final int CATEGORY_LIKE=1;
        public static final int CATEGORY_ADD_TO_CALENDAR=2;
        public static final int CATEGORY_REPORT=3;
        public static final int CATEGORY_COMMENTS=4;

        public static final String NOTIFICATION_CONTENT_ITEM_URI = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + NOTIFICATION_PATH_NAME;
        public static final String NOTIFICATION_CONTENT_LIST_URI = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + NOTIFICATION_PATH_NAME;
    }
}