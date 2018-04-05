package com.example.nive_pt1681.recipeapplication.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.adapter.PeopleRecyclerAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;

/**
 * Created by nive-pt1681 on 27/02/18.
 */

public class PeopleFragment extends Fragment {

    private RecyclerView mPeopleRecyclerView;
    private PeopleRecyclerAdapter mAdapter;
    private Uri mUri;
    private String[] projections={UserEntry._ID,UserEntry.USER_ID,UserEntry.USER_NAME,UserEntry.USER_COOKING_LEVEL,UserEntry.USER_CREDITS,UserEntry.USER_IMAGE};

    public static Fragment newInstance(){
        return new PeopleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.people_layout,container,false);
        mPeopleRecyclerView=view.findViewById(R.id.people_recycler_list);
        mPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new PeopleRecyclerAdapter(null,getActivity());
        mPeopleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPeopleRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public void displayPeople(){
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                mUri=UserEntry.CONTENT_URI;
                return new CursorLoader(getActivity(),mUri,projections,UserEntry.USER_ID+"!=?",new String[]{String.valueOf(HomeActivity.id)},null);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        displayPeople();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_faq,menu);
        MenuItem item=menu.findItem(R.id.faq_search);
        final SearchView searchViewActionBar= (SearchView) MenuItemCompat.getActionView(item);
        searchViewActionBar.setIconifiedByDefault(false);
        searchViewActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        Log.d("me", "onCreateOptionsMenu: " +
                "People Fragment");
        Log.d("me",menu.getItem(0).toString()+" "+menu.toString());
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
