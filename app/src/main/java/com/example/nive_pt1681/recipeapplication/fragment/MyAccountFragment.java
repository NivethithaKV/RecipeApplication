package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.activity.LoginActivity;
import com.example.nive_pt1681.recipeapplication.adapter.MyAccountViewPagerAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FollowEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;
import com.example.nive_pt1681.recipeapplication.model.User;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nive-pt1681 on 26/02/18.
 */

public class MyAccountFragment extends Fragment{

    private static User mUser;
    private TabLayout mTabLayout;
    private MyAccountViewPagerAdapter myAccountViewPagerAdapter;

    public static Fragment newInstance(User user){
        mUser=user;
        return new MyAccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_account,container,false);

        myAccountViewPagerAdapter=new MyAccountViewPagerAdapter(getChildFragmentManager(),mUser);

        ViewPager viewPager=view.findViewById(R.id.my_account_view_pager);
        viewPager.setAdapter(myAccountViewPagerAdapter);

        mTabLayout=view.findViewById(R.id.my_account_tabs);
        mTabLayout.setupWithViewPager(viewPager);

        Log.d("me", "onCreateView: "+mUser.getName());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_my_account,menu);
        Log.d("me", "onCreateOptionsMenu: " +
                "MyAccountFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.account_edit:
                return true;
            case R.id.account_logout:
                Log.d("me", "onOptionsItemSelected: Pressed");
                getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putString(LoginActivity.PREFS_MAIL,null)
                        .putString(LoginActivity.PREFS_PASSWORD,null)
                        .apply();
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
