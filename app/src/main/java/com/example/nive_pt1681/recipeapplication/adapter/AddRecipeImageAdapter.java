package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nive_pt1681.recipeapplication.R;

import java.util.ArrayList;

/**
 * Created by nive-pt1681 on 22/03/18.
 */

public class AddRecipeImageAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Uri> mUris;

    public AddRecipeImageAdapter(Context context, ArrayList<Uri> uris){
        mContext=context;
        mUris=uris;
    }

    @Override
    public int getCount() {
        return mUris.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.pager_item,container,false);
        ImageView imageView=view.findViewById(R.id.imageView);
        Uri filePath=mUris.get(position);
        Glide.with(mContext)
                .load(filePath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_person)
                        .error(R.drawable.default_person))
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
