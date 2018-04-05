package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.RecipeDetailActivity;
import com.example.nive_pt1681.recipeapplication.adapter.LeftBarListAdapter;

/**
 * Created by nive-pt1681 on 09/03/18.
 */

public class LeftBarFragment extends ListFragment{

    myScrollListener mScrollListener;

    public interface myScrollListener{
        public void scrollToFragment(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mScrollListener=(RecipeDetailActivity)context;
        }
        catch (Exception e){
            Log.d("me", "onAttach: ");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_left_bar,container,false);
        setListAdapter(new LeftBarListAdapter(R.layout.single_left_bar_item,getActivity()));
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mScrollListener.scrollToFragment(position);
    }
}
