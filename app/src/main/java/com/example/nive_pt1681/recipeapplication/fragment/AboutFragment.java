package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.model.User;


import static android.app.Activity.RESULT_OK;

/**
 * Created by nive-pt1681 on 28/02/18.
 */

public class AboutFragment extends Fragment {

    private static User sUser;
    private TextView mAccountName;
    private TextView mAccountMail;
    private TextView mAccountRegion;
    private TextView mAccountLevel;
    private TextView mAccountCredits;
    private TextView mFollowers;
    private TextView mFollowing;
    private ImageView mUserImage;
    private static final int RESULT_LOAD_IMAGE=0;

    public static Fragment newInstance(User user){
        sUser=user;
        return new AboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Cursor cursor=null;
        super.onCreate(savedInstanceState);
        try{
            cursor=getActivity().getContentResolver().query(RecipeContract.FollowEntry.CONTENT_URI,null, RecipeContract.FollowEntry.FOLLOWER+"=?",new String[]{sUser.getUUID().toString()},null);
            if(cursor!=null){
                sUser.setFollowing(cursor.getCount());
            }
            cursor=getActivity().getContentResolver().query(RecipeContract.FollowEntry.CONTENT_URI,null, RecipeContract.FollowEntry.FOLLOWING+"=?",new String[]{sUser.getUUID().toString()},null);
            if(cursor!=null){
                sUser.setFollower(cursor.getCount());
            }
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_about_layout, container, false);
        mFollowers = view.findViewById(R.id.no_of_followers);
        mFollowing = view.findViewById(R.id.no_of_following);

        mAccountName = view.findViewById(R.id.account_name);
        mAccountCredits = view.findViewById(R.id.account_credits);
        mAccountLevel = view.findViewById(R.id.account_level_id);
        mAccountMail = view.findViewById(R.id.account_mail_id);
        mAccountRegion = view.findViewById(R.id.account_region_id);

        mUserImage = view.findViewById(R.id.user_image);
        Glide.with(getContext())
                .load(sUser.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_person)
                        .error(R.drawable.default_person))
                .into(mUserImage);

        mAccountName.setText(sUser.getName());
        mAccountRegion.setText(getResources().getStringArray(R.array.region_array)[sUser.getRegion()]);
        mAccountMail.setText(sUser.getEmail());
        mAccountLevel.setText(getResources().getStringArray(R.array.cooking_level_array)[sUser.getCooking_level()]);
        mAccountCredits.setText(String.valueOf(sUser.getCredits()));
        mFollowers.setText(getString(R.string.followers, sUser.getFollower()));
        mFollowing.setText(getString(R.string.following, sUser.getFollowing()));

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMAGE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            if (selectedImg != null) {
                Glide.with(getActivity())
                        .load(selectedImg)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.default_person))
                        .into(mUserImage);
                sUser.setImage(selectedImg.toString());
                int updated = getActivity().getContentResolver().update(ContentUris.withAppendedId(RecipeContract.UserEntry.CONTENT_URI, sUser.getUser_id()), HomeActivity.userToContentValues(sUser), RecipeContract.UserEntry.USER_ID+"=?", new String[]{sUser.getUUID().toString()});
                if (updated == 0) {
                    Toast.makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Updated Successfully..."+sUser.getImage(), Toast.LENGTH_SHORT).show();
                    Log.d("me", "onActivityResult: "+sUser.getImage());
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked any image!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
