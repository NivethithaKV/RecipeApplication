package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.IngredientsEntry;

/**
 * Created by nive-pt1681 on 14/03/18.
 */

public class RecipeIngredientsFragment extends Fragment {

    private static String mRecipe_id;
    private TextView no_of_servings;
    private static int mServings;
    private LinearLayout parentLayout;

    public static Fragment newInstance(String recipe_id,int servings){
        mRecipe_id=recipe_id;
        mServings=servings;
        return new RecipeIngredientsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recipe_detail_ingredients,container,false);
        no_of_servings=view.findViewById(R.id.no_of_servings);

        no_of_servings.setText(getString(R.string.no_of_servings,mServings));

        Cursor cursor=getActivity().getContentResolver().query(IngredientsEntry.CONTENT_URI,null, IngredientsEntry.INGREDIENTS_RECIPE_ID+"=?",new String[]{mRecipe_id},null);
        LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentLayout=view.findViewById(R.id.parent_of_ingredients);

        try{
            if(cursor!= null && cursor.moveToFirst()){
                do{
                    View singleIngredient=layoutInflater.inflate(R.layout.single_ingredient_layout,null);
                    TextView quantity=singleIngredient.findViewById(R.id.ingredient_quantity);
                    quantity.setText(getString(R.string.ingredient_text,cursor.getString(cursor.getColumnIndex(IngredientsEntry.INGREDIENTS_NAME))));
                    parentLayout.addView(singleIngredient,parentLayout.getChildCount()-1);
                }while(cursor.moveToNext());
            }
        }
        finally {
            cursor.close();
        }
        return view;
    }
}
