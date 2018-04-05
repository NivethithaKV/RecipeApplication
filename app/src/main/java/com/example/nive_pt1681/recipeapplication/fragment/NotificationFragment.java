package com.example.nive_pt1681.recipeapplication.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.adapter.NotificationAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;

/**
 * Created by nive-pt1681 on 23/03/18.
 */

public class NotificationFragment extends AppCompatActivity {

    private RecyclerView mNotificationRecyclerView;
    private NotificationAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_fragment);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNotificationRecyclerView = findViewById(R.id.notifications);
        mNotificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NotificationAdapter(null, this);
        mNotificationRecyclerView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(), NotificationEntry.CONTENT_URI, null, NotificationEntry.NOTIFICATION_TO_USER_ID + "=?", new String[]{HomeActivity.id}, null);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
