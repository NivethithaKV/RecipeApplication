package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.example.nive_pt1681.recipeapplication.activity.AddQuestionActivity;
import com.example.nive_pt1681.recipeapplication.adapter.FAQAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.FAQQuestionsEntry;

/**
 * Created by nive-pt1681 on 27/02/18.
 */

public class FAQFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private FAQAdapter mAdapter;

    public static Fragment newInstance(){
        return new FAQFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.faq_fragment,container,false);
        mRecyclerView=view.findViewById(R.id.faq_list);
        fab=view.findViewById(R.id.faq_fab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new FAQAdapter(null,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddQuestionActivity.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(), FAQQuestionsEntry.CONTENT_URI,null,null,null, FAQQuestionsEntry.QUESTIONS_DATE);
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
        MenuItem item=menu.findItem(R.id.faq_search);
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
        Log.d("me", "onCreateOptionsMenu: " +
                "FAQFragment");
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
