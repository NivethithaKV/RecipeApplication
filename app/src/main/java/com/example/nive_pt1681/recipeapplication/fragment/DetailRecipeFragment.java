package com.example.nive_pt1681.recipeapplication.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;
import com.example.nive_pt1681.recipeapplication.model.Recipe;

/**
 * Created by nive-pt1681 on 12/03/18.
 */

public class DetailRecipeFragment extends Fragment {
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mRecipeSecurity;
    private TextView mRecipeDescription;
    private static Recipe mRecipe;
    private ImageView mRecipeDietIndicator;
    private TextView mRecipeDiet;
    private TextView mRecipeCredits;

    public static Fragment newInstance(Recipe recipe){
        mRecipe=recipe;
        return new DetailRecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recipe_detail_info,container,false);
        mUserImage=view.findViewById(R.id.user_image);
        mUserName=view.findViewById(R.id.recipe_by_user_name);
        mRecipeSecurity=view.findViewById(R.id.shared_message);
        mRecipeDescription=view.findViewById(R.id.recipe_description);
        mRecipeDiet=view.findViewById(R.id.diet);
        mRecipeDietIndicator=view.findViewById(R.id.diet_indicator);
        mRecipeCredits=view.findViewById(R.id.earned_credits);

        mUserImage.requestFocus(View.FOCUS_UP);

        Cursor cursor=null;
        try{
            cursor=getActivity().getContentResolver().query(UserEntry.CONTENT_URI,new String[]{UserEntry.USER_ID,UserEntry.USER_NAME,UserEntry.USER_IMAGE},UserEntry.USER_ID+"=?",new String[]{mRecipe.getRecipe_by_user_id()},null);

            if(cursor!=null && cursor.moveToFirst()){
                mUserName.setText(cursor.getString(cursor.getColumnIndex(UserEntry.USER_NAME)));
                Glide.with(this)
                        .load(cursor.getString(cursor.getColumnIndex(UserEntry.USER_IMAGE)))
                        .into(mUserImage);
                mRecipeSecurity.setText(findSecurity(mRecipe.getRecipe_security()));
                String description="\t"+"\t"+"\t"+mRecipe.getRecipe_description();
                mRecipeDescription.setText(description);
                int diet=mRecipe.getRecipe_diet();
                setRecipeDiet(diet);
                mRecipeCredits.setText(getString(R.string.people_credits_text,mRecipe.getRecipe_earned_credits()));
            }
        }
        finally {
            cursor.close();
        }

        return view;
    }

    private String findSecurity(int security){
        switch (security){
            case RecipeEntry.SECURITY_PUBLIC:
                return "Shared To Public";
            case RecipeEntry.SECURITY_MY_RECIPES:
                return "Saved To My Recipes";
            default:
                return "Shared As Private";
        }
    }

    private void setRecipeDiet(int diet){
        switch (diet){
            case RecipeEntry.RECIPE_DIET_VEGAN:
                mRecipeDietIndicator.setImageResource(android.R.color.holo_orange_dark);
                mRecipeDiet.setText(getString(R.string.diet_vegan));
                break;
            case RecipeEntry.RECIPE_DIET_VEGETARIAN:
                mRecipeDietIndicator.setImageResource(android.R.color.holo_green_dark);
                mRecipeDiet.setText(getString(R.string.diet_vegetarian));
                break;
            case RecipeEntry.RECIPE_DIET_NON_VEGETARIAN:
                mRecipeDietIndicator.setImageResource(android.R.color.holo_red_dark);
                mRecipeDiet.setText(getString(R.string.diet_non_vegetarian));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
