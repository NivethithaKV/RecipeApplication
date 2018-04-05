package com.example.nive_pt1681.recipeapplication.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.CalendarEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 21/03/18.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static String recipeId;

    public static DialogFragment newInstance(String recipe_id){
        recipeId=recipe_id;
        return new DatePickerFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        int year1=calendar.get(Calendar.YEAR);
        int month1=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        if(year>=year1 && month>=month1 && dayOfMonth>=day){
            Cursor cursor=null;
            try{
                cursor=getActivity().getContentResolver().query(CalendarEntry.CONTENT_URI,new String[]{"MAX("+ CalendarEntry._ID+") AS MAX_ID"},null,null,null);
                if(cursor!=null && cursor.moveToFirst()){
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(CalendarEntry.CALENDAR_ID, UUID.randomUUID().toString());
                    contentValues.put(CalendarEntry.CALENDAR_USER_ID, HomeActivity.id);
                    contentValues.put(CalendarEntry.CALENDAR_RECIPE_ID,recipeId);
                    Long date=new Date(year,month,dayOfMonth).getTime();
                    contentValues.put(CalendarEntry.CALENDAR_DATE,String.valueOf(date));
                    int count=cursor.getInt(0);
                    contentValues.put(CalendarEntry._ID,count+1);
                    Uri uri=getActivity().getContentResolver().insert(CalendarEntry.CONTENT_URI,contentValues);
                    if(uri!=null){
                        Toast.makeText(getActivity(),"Inserted successfully",Toast.LENGTH_SHORT).show();
                    }
                }

                ContentValues values=new ContentValues();
                cursor=getActivity().getContentResolver().query(NotificationEntry.CONTENT_URI,new String[]{"MAX(" + RecipeContract.FavoritesEntry._ID + ") AS max_id"}, null, null, null);
                if(cursor!=null && cursor.moveToFirst()){
                    int count=cursor.getInt(0);
                    values.put(NotificationEntry._ID,count+1);
                    values.put(NotificationEntry.NOTIFICATION_CATEGORY, NotificationEntry.CATEGORY_ADD_TO_CALENDAR);
                    values.put(NotificationEntry.NOTIFICATION_ID, UUID.randomUUID().toString());
                    values.put(NotificationEntry.NOTIFICATION_TO_USER_ID,HomeActivity.id);
                    values.put(NotificationEntry.NOTIFICATION_RECIPE_ID,recipeId);
                    values.put(NotificationEntry.NOTIFICATION_MESSAGE,"You marked your calendar!!! You marked "+dayOfMonth+"/"+(month+1)+"/"+year);
                    getActivity().getContentResolver().insert(NotificationEntry.CONTENT_URI,values);
                }
            }
            finally {
                cursor.close();
            }
        }
    }
}
