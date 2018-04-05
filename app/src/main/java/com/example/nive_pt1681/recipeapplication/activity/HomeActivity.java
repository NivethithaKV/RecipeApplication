package com.example.nive_pt1681.recipeapplication.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FollowEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;
import com.example.nive_pt1681.recipeapplication.fragment.FAQFragment;
import com.example.nive_pt1681.recipeapplication.fragment.HomeFragment;
import com.example.nive_pt1681.recipeapplication.fragment.MyAccountFragment;
import com.example.nive_pt1681.recipeapplication.fragment.MyCookBookFragment;
import com.example.nive_pt1681.recipeapplication.fragment.PeopleFragment;
import com.example.nive_pt1681.recipeapplication.model.User;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.security.Permission;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 23/02/18.
 */

public class HomeActivity extends AppCompatActivity {

    public static final String TAG="me";
    public static final int PERMISSIONS_READ_STORAGE=0;
    public static String id;
    private boolean backPressedOnce=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_READ_STORAGE);
        }
        else{
            displayHome();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(backPressedOnce){
            moveTaskToBack(true);
            return;
        }

        backPressedOnce=true;
        Toast.makeText(this,"Press again to exit...",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce=false;
            }
        },2000);
    }

    private User getUser(String id){
        User user=new User();
        Cursor cursor=null;
        if(id!=null){
            try{
                cursor=getContentResolver().query(UserEntry.CONTENT_URI,null, UserEntry.USER_ID+"=?",new String[]{String.valueOf(id)},null);
                if(cursor!=null && cursor.moveToFirst()){
                    user.setUser_id(cursor.getInt(cursor.getColumnIndex(UserEntry._ID)));
                    user.setUUID(UUID.fromString(cursor.getString(cursor.getColumnIndex(UserEntry.USER_ID))));
                    user.setName(cursor.getString(cursor.getColumnIndex(UserEntry.USER_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(UserEntry.USER_EMAIL)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(UserEntry.USER_PASSWORD)));
                    user.setRegion(cursor.getInt(cursor.getColumnIndex(UserEntry.USER_REGION)));
                    user.setCooking_level(cursor.getInt(cursor.getColumnIndex(UserEntry.USER_COOKING_LEVEL)));
                    user.setImage(cursor.getString(cursor.getColumnIndex(UserEntry.USER_IMAGE)));
                    user.setCredits(cursor.getInt(cursor.getColumnIndex(UserEntry.USER_CREDITS)));
                }
                cursor=getContentResolver().query(FollowEntry.CONTENT_URI,null,FollowEntry.FOLLOWER+"=?",new String[]{String.valueOf(id)},null);
                if(cursor!=null){
                    user.setFollower(cursor.getCount());
                }
                else{
                    user.setFollower(0);
                }
                cursor=getContentResolver().query(FollowEntry.CONTENT_URI,null,FollowEntry.FOLLOWING+"=?",new String[]{String.valueOf(id)},null);
                if(cursor!=null){
                    user.setFollowing(cursor.getCount());
                }
                else{
                    user.setFollowing(0);
                }
            }
            finally {
                cursor.close();
            }
        }
        return user;
    }

    public static ContentValues userToContentValues(User user){
        ContentValues contentValues=new ContentValues();
        contentValues.put(UserEntry._ID,user.getUser_id());
        contentValues.put(UserEntry.USER_ID,user.getUUID().toString());
        contentValues.put(UserEntry.USER_NAME,user.getName());
        contentValues.put(UserEntry.USER_EMAIL,user.getEmail());
        contentValues.put(UserEntry.USER_PASSWORD,user.getPassword());
        contentValues.put(UserEntry.USER_COOKING_LEVEL,user.getCooking_level());
        contentValues.put(UserEntry.USER_REGION,user.getRegion());
        contentValues.put(UserEntry.USER_CREDITS,user.getCredits());
        contentValues.put(UserEntry.USER_IMAGE,user.getImage());
        return contentValues;
    }

    public static ContentValues followToContentValues(String followingId,int id){
        ContentValues contentValues=new ContentValues();
        contentValues.put(FollowEntry._ID,id);
        contentValues.put(FollowEntry.FOLLOWER,HomeActivity.id);
        contentValues.put(FollowEntry.FOLLOWING,followingId);
        return contentValues;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_READ_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                displayHome();
            } else {
                Toast.makeText(this,"You should grant this permissions for the application to start",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void displayHome(){
        id=getIntent().getStringExtra("user_id");
        final User mUser=getUser(id);

        final Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        BottomBar bottomBar=findViewById(R.id.bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                setSupportActionBar(toolbar);
                switch (tabId){
                    case R.id.home:
                        Log.d(TAG, "onTabSelected: image url: "+mUser.getImage()+" "+mUser.getUUID().toString());
                        toolbar.setTitle("Home");
                        Log.d(TAG, "onTabSelected: home");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,HomeFragment.newInstance()).commit();
                        break;
                    case R.id.faq:
                        Log.d(TAG, "onTabSelected: faq");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,FAQFragment.newInstance()).commit();
                        toolbar.setTitle("FAQ");
                        break;
                    case R.id.people:
                        Log.d(TAG, "onTabSelected: people");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,PeopleFragment.newInstance()).commit();
                        toolbar.setTitle("People");
                        break;
                    case R.id.my_book:
                        Log.d(TAG, "onTabSelected: "+UUID.randomUUID().toString());
                        Log.d(TAG, "onTabSelected: my_book");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,MyCookBookFragment.newInstance()).commit();
                        toolbar.setTitle("My CookBook");
                        break;
                    case R.id.my_account:
                        Log.d(TAG, "onTabSelected: my_account");
                        toolbar.setTitle("My Account");
                        Fragment fragment=MyAccountFragment.newInstance(mUser);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                        break;
                }
            }
        });
    }

}
