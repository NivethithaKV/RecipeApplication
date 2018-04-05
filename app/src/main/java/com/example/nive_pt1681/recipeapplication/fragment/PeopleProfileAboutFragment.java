package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;
import com.example.nive_pt1681.recipeapplication.model.User;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 15/03/18.
 */

public class PeopleProfileAboutFragment extends Fragment {

    private TextView mPeopleName;
    private TextView mPeopleCookingLevel;
    private TextView mPeopleCredits;
    private TextView mFollowers;
    private TextView mFollowing;
    private ToggleButton mFollowButton;
    private static User mUser;

    public static Fragment newInstance(User user){
        mUser=user;
        return new PeopleProfileAboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.people_profile_about,container,false);
        mPeopleName=view.findViewById(R.id.account_name);
        mPeopleCookingLevel=view.findViewById(R.id.account_level_id);
        mPeopleCredits=view.findViewById(R.id.account_credits);
        mFollowButton=view.findViewById(R.id.follow_button);
        mFollowers=view.findViewById(R.id.no_of_followers);
        mFollowing=view.findViewById(R.id.no_of_following);
        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFollowButton.getText().equals(mFollowButton.getTextOff())) {
                    int rowsDeleted=getActivity().getContentResolver().delete(RecipeContract.FollowEntry.CONTENT_URI, RecipeContract.FollowEntry.FOLLOWER+"=? AND "+ RecipeContract.FollowEntry.FOLLOWING+"=?",new String[]{HomeActivity.id,mUser.getUUID().toString()});
                    if(rowsDeleted==0){
                        Toast.makeText(getActivity(),"No Records Found...",Toast.LENGTH_SHORT).show();
                    }
                    if(rowsDeleted==1)
                        Toast.makeText(getActivity(),"Record deleted successfully...",Toast.LENGTH_SHORT).show();
                    ContentValues contentValues=new ContentValues();
                    int credits=mUser.getCredits();
                    credits-=10;
                    contentValues.put(RecipeContract.UserEntry.USER_CREDITS,credits);
                    int rowsUpdated=getActivity().getContentResolver().update(RecipeContract.UserEntry.CONTENT_URI,contentValues, RecipeContract.UserEntry.USER_ID+"=?",new String[]{mUser.getUUID().toString()});
                    if(rowsUpdated==1){
                        mUser.setCredits(credits);
                    }
                    setUpFollowers();
                }
                else{
                    int count = 0;
                    Cursor cursor = getActivity().getContentResolver().query(RecipeContract.FollowEntry.CONTENT_URI,new String[]{"MAX("+ RecipeContract.FollowEntry._ID+") AS max_id"},null,null,null);
                    if (cursor != null && cursor.moveToFirst()) {
                        count = cursor.getInt(0);
                        cursor.close();
                    }
                    Uri uri = getActivity().getContentResolver().insert(RecipeContract.FollowEntry.CONTENT_URI, HomeActivity.followToContentValues(mUser.getUUID().toString(), count + 1));
                    if (uri == null) {
                        Toast.makeText(getActivity(), "Not inserted successfully...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Record inserted successfully...", Toast.LENGTH_SHORT).show();
                    }
                    ContentValues contentValues=new ContentValues();
                    int credits=mUser.getCredits();
                    credits+=10;
                    contentValues.put(RecipeContract.UserEntry.USER_CREDITS,credits);
                    int rowsUpdated=getActivity().getContentResolver().update(RecipeContract.UserEntry.CONTENT_URI,contentValues, RecipeContract.UserEntry.USER_ID+"=?",new String[]{mUser.getUUID().toString()});
                    if(rowsUpdated==1){
                        mUser.setCredits(credits);
                    }
                    setUpFollowers();
                    ContentValues values=new ContentValues();
                    cursor=getActivity().getContentResolver().query(NotificationEntry.CONTENT_URI,new String[]{"MAX(" + RecipeContract.FavoritesEntry._ID + ") AS max_id"}, null, null, null);
                    if(cursor!=null && cursor.moveToFirst()){
                        count=cursor.getInt(0);
                        values.put(NotificationEntry._ID,count+1);
                        values.put(NotificationEntry.NOTIFICATION_CATEGORY, NotificationEntry.CATEGORY_FOLLOW);
                        values.put(NotificationEntry.NOTIFICATION_ID, UUID.randomUUID().toString());
                        values.put(NotificationEntry.NOTIFICATION_TO_USER_ID,mUser.getUUID().toString());
                        values.put(NotificationEntry.NOTIFICATION_MESSAGE,mUser.getName()+" started Following you!!!");
                        getActivity().getContentResolver().insert(NotificationEntry.CONTENT_URI,values);
                    }
                }
            }
        });

        setUpFollowers();
        mPeopleName.setText(mUser.getName());
        mPeopleCredits.setText(getResources().getString(R.string.people_credits_text,mUser.getCredits()));
        String me= (getResources().getStringArray(R.array.cooking_level_array))[mUser.getCooking_level()];
        mPeopleCookingLevel.setText(me);
        Cursor cursor=null;
        try{
            cursor=getActivity().getContentResolver().query(RecipeContract.FollowEntry.CONTENT_URI,null, RecipeContract.FollowEntry.FOLLOWER+"=? AND "+ RecipeContract.FollowEntry.FOLLOWING+"=?",new String[]{HomeActivity.id,mUser.getUUID().toString()},null);
            if(cursor!=null && cursor.moveToFirst()){
                mFollowButton.setChecked(true);
            }
            else{
                mFollowButton.setChecked(false);
            }
        }
        finally {
            cursor.close();
        }
        return view;
    }

    private void setUpFollowers(){
        Cursor cursor=null;
        try{
            cursor=getActivity().getContentResolver().query(RecipeContract.FollowEntry.CONTENT_URI,null, RecipeContract.FollowEntry.FOLLOWER+"=?",new String[]{mUser.getUUID().toString()},null);
            if(cursor!=null){
                mFollowing.setText(getString(R.string.following,cursor.getCount()));
            }
            cursor=getActivity().getContentResolver().query(RecipeContract.FollowEntry.CONTENT_URI,null, RecipeContract.FollowEntry.FOLLOWING+"=?",new String[]{mUser.getUUID().toString()},null);
            if(cursor!=null){
                mFollowers.setText(getString(R.string.followers,cursor.getCount()));
            }
            mPeopleCredits.setText(getResources().getString(R.string.people_credits_text,mUser.getCredits()));
        }
        finally {
            cursor.close();
        }
    }
}
