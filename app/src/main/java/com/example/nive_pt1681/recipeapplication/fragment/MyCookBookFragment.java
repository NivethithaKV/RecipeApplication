package com.example.nive_pt1681.recipeapplication.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.adapter.MyCookBookAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FavoritesEntry;

/**
 * Created by nive-pt1681 on 27/02/18.
 */

public class MyCookBookFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MyCookBookAdapter mAdapter;

    public static Fragment newInstance(){
        return new MyCookBookFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.home_layout,container,false);
        FloatingActionButton floatingActionButton=view.findViewById(R.id.add_recipe_fab);
        floatingActionButton.setVisibility(View.GONE);
        mRecyclerView=view.findViewById(R.id.home_recipe_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new MyCookBookAdapter(null,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(),FavoritesEntry.CONTENT_URI,null,FavoritesEntry.FAVORITES_USER_ID+"=?",new String[]{HomeActivity.id},FavoritesEntry._ID+" DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdapter.setInitialCursor(data);
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_faq,menu);
        Log.d("me", "onCreateOptionsMenu: " +
                "MyCookBookFragment");
        Log.d("me",menu.getItem(0).toString()+ " "+menu.toString());
        //Log.d("me",menu.getItem(1).toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.faq_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
