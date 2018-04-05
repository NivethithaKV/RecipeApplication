package com.example.nive_pt1681.recipeapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nive_pt1681.recipeapplication.fragment.MyRecipesFragment;
import com.example.nive_pt1681.recipeapplication.fragment.PeopleProfileAboutFragment;
import com.example.nive_pt1681.recipeapplication.model.User;

/**
 * Created by nive-pt1681 on 15/03/18.
 */

public class PeopleProfileViewPagerAdapter extends FragmentPagerAdapter{

    private User mUser;

    public PeopleProfileViewPagerAdapter(FragmentManager fm,User user) {
        super(fm);
        mUser=user;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position==0){
            fragment= PeopleProfileAboutFragment.newInstance(mUser);
        }
        if(position==1){
            fragment= MyRecipesFragment.newInstance(mUser.getUUID().toString());
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "About";
        }
        if(position==1){
            return "Collections";
        }
        return "";
    }
}
