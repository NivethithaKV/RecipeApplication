package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.database.Cursor;
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
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;

/**
 * Created by nive-pt1681 on 08/03/18.
 */

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private Cursor mCursor;

    public ImageAdapter(Context context,Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
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
        mCursor.moveToPosition(position);
        String filePath=mCursor.getString(mCursor.getColumnIndex(ImageEntry.IMAGES_URI));
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
