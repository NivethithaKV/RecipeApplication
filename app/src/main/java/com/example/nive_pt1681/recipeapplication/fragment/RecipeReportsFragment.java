package com.example.nive_pt1681.recipeapplication.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.adapter.ReportsAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReportsEntry;

/**
 * Created by nive-pt1681 on 16/03/18.
 */

public class RecipeReportsFragment extends Fragment {

    private static String recipeId;
    private RecyclerView mReportedRecyclerView;
    private ReportsAdapter mAdapter;

    public static Fragment newInstance(String recipe_id){
        recipeId=recipe_id;
        return new RecipeReportsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recipe_detail_reports,container,false);
        mReportedRecyclerView=view.findViewById(R.id.reports_list);
        mReportedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new ReportsAdapter(null,getActivity());
        mReportedRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(), ReportsEntry.CONTENT_URI,null,ReportsEntry.REPORTS_RECIPE_ID+"=?",new String[]{recipeId},ReportsEntry.REPORTS_DATE+" DESC");
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

