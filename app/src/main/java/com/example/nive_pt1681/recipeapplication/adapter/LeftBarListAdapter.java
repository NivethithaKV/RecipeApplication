package com.example.nive_pt1681.recipeapplication.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;

/**
 * Created by nive-pt1681 on 09/03/18.
 */

public class LeftBarListAdapter extends ArrayAdapter<String> {

    private Activity mActivity;
    private int resource;
    private String icons[]={"ic_about.png","ic_ingredients.png","ic_methods.png","ic_video.png","ic_reports.png","ic_comments.png"};


    public LeftBarListAdapter(int single_left_bar_item, Activity activity){
        super(activity, single_left_bar_item);
        mActivity=activity;
        resource=single_left_bar_item;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=mActivity.getLayoutInflater().inflate(resource,parent,false);
            LeftBarViewHolder holder=new LeftBarViewHolder();

            holder.mImageView=convertView.findViewById(R.id.left_bar_icon);

            convertView.setTag(holder);
        }

        LeftBarViewHolder holder=(LeftBarViewHolder)convertView.getTag();
        Glide.with(mActivity)
                .load("/storage/emulated/0/Recipe/"+icons[position])
                .into(holder.mImageView);
        return convertView;
    }

    private static class LeftBarViewHolder{
        private ImageView mImageView;
    }
}
