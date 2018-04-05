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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.adapter.MyRecipesAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;

/**
 * Created by nive-pt1681 on 28/02/18.
 */

/**
 * Created by nive-pt1681 on 27/02/18.
 */

public class MyRecipesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MyRecipesAdapter mAdapter;
    private static String user_id;

    public static Fragment newInstance(String id){
        user_id=id;
        return new MyRecipesFragment();
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
        mAdapter=new MyRecipesAdapter(null,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(),RecipeEntry.CONTENT_URI,null,RecipeEntry.RECIPE_BY_USER_ID+"=?",new String[]{user_id},RecipeEntry._ID+" DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
        return view;
    }
}
