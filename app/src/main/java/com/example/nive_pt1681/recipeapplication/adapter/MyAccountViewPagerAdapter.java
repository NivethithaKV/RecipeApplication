package com.example.nive_pt1681.recipeapplication.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nive_pt1681.recipeapplication.fragment.AboutFragment;
import com.example.nive_pt1681.recipeapplication.fragment.MyAccountFragment;
import com.example.nive_pt1681.recipeapplication.fragment.MyRecipesFragment;
import com.example.nive_pt1681.recipeapplication.model.User;

/**
 * Created by nive-pt1681 on 28/02/18.
 */

public class MyAccountViewPagerAdapter extends FragmentPagerAdapter {

    private String titles[]={"About","My Recipes"};
    private static User sUser;

    public MyAccountViewPagerAdapter(FragmentManager fm,User user) {
        super(fm);
        sUser=user;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position==0){
            fragment= AboutFragment.newInstance(sUser);
        }
        else if(position==1){
            fragment= MyRecipesFragment.newInstance(sUser.getUUID().toString());
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
        return titles[position];
    }
}
