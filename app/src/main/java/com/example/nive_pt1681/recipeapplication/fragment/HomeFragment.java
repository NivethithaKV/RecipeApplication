package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.view.inputmethod.InputMethodManager;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.AddRecipeActivity;
import com.example.nive_pt1681.recipeapplication.activity.FiltersActivity;
import com.example.nive_pt1681.recipeapplication.adapter.RecipeHomeRecyclerAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.model.User;

/**
 * Created by nive-pt1681 on 26/02/18.
 */

public class HomeFragment extends Fragment {

    private RecyclerView mHomeRecipeRecyclerView;
    private RecipeHomeRecyclerAdapter mAdapter;
    private FloatingActionButton fab;

    public static Fragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout,container,false);
        mHomeRecipeRecyclerView=view.findViewById(R.id.home_recipe_list);
        fab=view.findViewById(R.id.add_recipe_fab);
        mHomeRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mAdapter=new RecipeHomeRecyclerAdapter(null,getActivity());
        mHomeRecipeRecyclerView.setAdapter(mAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(),RecipeEntry.CONTENT_URI,null,null,null,null);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                mAdapter.setInitialCursor(data);
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home,menu);
        MenuItem item=menu.findItem(R.id.home_search);
        final SearchView searchViewActionBar= (SearchView) MenuItemCompat.getActionView(item);
        searchViewActionBar.setIconifiedByDefault(true);
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
        Log.d("me",menu.getItem(0).toString()+" "+menu.toString());
        Log.d("me",menu.getItem(1).toString()+" "+menu.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.filters:
                Intent filtersIntent=new Intent(getActivity(), FiltersActivity.class);
                startActivity(filtersIntent);
                break;
            case R.id.home_notifications:
                Intent intent=new Intent(getActivity(),NotificationFragment.class);
                startActivity(intent);
                return true;
            case R.id.home_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
