package com.example.nive_pt1681.recipeapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MethodsEntry;

/**
 * Created by nive-pt1681 on 15/03/18.
 */

public class RecipeMethodsFragment extends Fragment {

    private LinearLayout parentOfMethods;
    private TextView timeTaken;
    private static String recipe_id,timeToCook;
    private Cursor mCursor;

    public static Fragment newInstance(String id,String time){
        recipe_id=id;
        timeToCook=time;
        return new RecipeMethodsFragment();
    }

    @SuppressLint("StaticFieldLeak")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recipe_detail_methods,container,false);
        parentOfMethods=view.findViewById(R.id.parent_of_methods);
        timeTaken=view.findViewById(R.id.time_taken);

        timeTaken.setText(timeToCook);

        mCursor=getActivity().getContentResolver().query(MethodsEntry.CONTENT_URI,null,MethodsEntry.METHODS_RECIPE_ID+"=?",new String[]{recipe_id},MethodsEntry._ID);
        try{
            LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(mCursor!=null && mCursor.moveToFirst()){
                do{
                    String description=mCursor.getString(mCursor.getColumnIndex(MethodsEntry.METHODS_DESCRIPTION));
                    View rowMethod=layoutInflater.inflate(R.layout.single_method_layout,null);
                    ImageView imageView=rowMethod.findViewById(R.id.method_image);
                    TextView methodDescription=rowMethod.findViewById(R.id.method_description);
                    if(description!=null){
                        methodDescription.setText(description);
                    }
                    if("#image#".equals(description)){
                        methodDescription.setVisibility(View.GONE);
                    }
                    String imagePath=mCursor.getString(mCursor.getColumnIndex(MethodsEntry.METHODS_IMAGE));
                    if(imagePath!=null){
                        Glide.with(getActivity())
                                .load(imagePath)
                                .into(imageView);
                    }
                    else {
                        imageView.setVisibility(View.GONE);
                    }
                    parentOfMethods.addView(rowMethod,parentOfMethods.getChildCount());
                }while(mCursor.moveToNext());
            }
        }
        finally {
            mCursor.close();
        }
        return view;
    }
}
