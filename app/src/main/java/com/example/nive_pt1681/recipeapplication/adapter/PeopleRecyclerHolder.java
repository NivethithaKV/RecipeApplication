package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.PeopleProfileActivity;
import com.example.nive_pt1681.recipeapplication.model.User;

/**
 * Created by nive-pt1681 on 02/03/18.
 */

public class PeopleRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView mPeopleName;
    private TextView mPeopleCookingLevel;
    private TextView mPeopleCredits;
    private ImageView mPeopleImage;
    private Context mContext;
    private User mUser;

    public PeopleRecyclerHolder(final View itemView, Context context) {
        super(itemView);
        mPeopleName=itemView.findViewById(R.id.people_name);
        mPeopleImage=itemView.findViewById(R.id.people_image);
        mPeopleCookingLevel=itemView.findViewById(R.id.people_cooking_level);
        mPeopleCredits=itemView.findViewById(R.id.people_credits);
        mContext=context;
        itemView.setOnClickListener(this);
    }

    public void bind(User user){
        mUser=user;
        mPeopleName.setText(mUser.getName());
        mPeopleCredits.setText(mContext.getResources().getString(R.string.people_credits_text,mUser.getCredits()));
        String me= (mContext.getResources().getStringArray(R.array.cooking_level_array))[mUser.getCooking_level()];
        mPeopleCookingLevel.setText(me);
        Glide.with(mContext)
                .load(mUser.getImage())
                .apply(new RequestOptions()
                .placeholder(R.drawable.default_person)
                .error(R.drawable.default_person))
                .into(mPeopleImage);
    }

    @Override
    public void onClick(View v) {
        if(mUser!=null){
            PeopleProfileActivity.getUser(mUser);
            Intent intent=new Intent(mContext.getApplicationContext(),PeopleProfileActivity.class);
            mContext.startActivity(intent);
        }
    }

}
