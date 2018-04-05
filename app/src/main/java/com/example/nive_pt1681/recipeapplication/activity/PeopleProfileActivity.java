package com.example.nive_pt1681.recipeapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.adapter.PeopleProfileViewPagerAdapter;
import com.example.nive_pt1681.recipeapplication.model.User;

/**
 * Created by nive-pt1681 on 05/03/18.
 */

public class PeopleProfileActivity extends AppCompatActivity {

    private ImageView mPeopleImage;
    private static User mUser;

    public static void getUser(User user){
        mUser=user;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_profile_page);

        Toolbar toolbar=findViewById(R.id.my_account_toolbar);
        toolbar.setTitle(mUser.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout=findViewById(R.id.people_profile_tabs);
        PeopleProfileViewPagerAdapter adapter=new PeopleProfileViewPagerAdapter(getSupportFragmentManager(),mUser);
        ViewPager viewPager=findViewById(R.id.people_profile_view_pager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        mPeopleImage=findViewById(R.id.people_profile_image);

        Glide.with(this)
                .load(mUser.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_person)
                        .error(R.drawable.default_person))
                .into(mPeopleImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
