package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;
import com.example.nive_pt1681.recipeapplication.model.User;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 02/03/18.
 */

public class PeopleRecyclerAdapter extends RecyclerView.Adapter<PeopleRecyclerHolder> implements Filterable{

    private String person_name,person_image;
    private int person_credits,person_cooking_level;
    private Cursor mCursor;
    private Cursor filteredCursor;
    private Cursor initialCursor;
    private boolean mDataValid;
    private Context mContext;
    private DataSetObserver mDataSetObserver;
    private int id;
    protected MyFilterClass mMyFilterClass;
    private String[] projections={UserEntry._ID,UserEntry.USER_ID,UserEntry.USER_NAME,UserEntry.USER_COOKING_LEVEL,UserEntry.USER_CREDITS,UserEntry.USER_IMAGE};

    public PeopleRecyclerAdapter(Cursor cursor, Context context){
        mContext=context;
        mCursor=cursor;
        mDataValid=cursor!=null;
        id=mDataValid? mCursor.getColumnIndex(UserEntry._ID):-1;
        mDataSetObserver=new NotifyDataSetObserver();
        if(mCursor!=null){
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }


    private class NotifyDataSetObserver extends  DataSetObserver{
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid=true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid=false;
            notifyDataSetChanged();
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public void swapCursor(Cursor newCursor){
        if(newCursor==mCursor){
            return;
        }
        mCursor=newCursor;
        if(mCursor!=null){
            id=newCursor.getColumnIndex(UserEntry._ID);
            mDataValid=true;
            notifyDataSetChanged();
        }
        else{
            id=-1;
            mDataValid=false;
            notifyDataSetChanged();
        }
    }

    @Override
    public PeopleRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_single_item,parent,false);
        return new PeopleRecyclerHolder(view,view.getContext());
    }

    @Override
    public void onBindViewHolder(PeopleRecyclerHolder holder, int position) {
        if(mCursor.moveToPosition(position)){
            id=mCursor.getInt(mCursor.getColumnIndex(UserEntry._ID));
            person_name=mCursor.getString(mCursor.getColumnIndex(UserEntry.USER_NAME));
            person_cooking_level=mCursor.getInt(mCursor.getColumnIndex(UserEntry.USER_COOKING_LEVEL));
            person_credits=mCursor.getInt(mCursor.getColumnIndex(UserEntry.USER_CREDITS));
            person_image=mCursor.getString(mCursor.getColumnIndex(UserEntry.USER_IMAGE));
            User user=new User();
            user.setUser_id(id);
            user.setName(person_name);
            user.setCooking_level(person_cooking_level);
            user.setCredits(person_credits);
            user.setImage(person_image);
            user.setUUID(UUID.fromString(mCursor.getString(mCursor.getColumnIndex(UserEntry.USER_ID))));
            holder.bind(user);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataValid && mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(mMyFilterClass==null){
            return new MyFilterClass();
        }
        return mMyFilterClass;
    }

    @Override
    public long getItemId(int position) {
        if(mDataValid && mCursor!=null && mCursor.moveToPosition(position)){
            return mCursor.getLong(id);
        }
        return 0;
    }


    private class MyFilterClass extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                filteredCursor=mContext.getContentResolver().query(UserEntry.CONTENT_URI,projections,UserEntry.USER_NAME+" LIKE ? AND "+UserEntry.USER_ID+"!=?",new String[]{"%"+constraint+"%", HomeActivity.id},UserEntry.USER_NAME);
                Log.d(HomeActivity.TAG, "performFiltering: "+HomeActivity.id);
                if(filteredCursor==null)
                    Log.d("me", "performFiltering: null returned");
                results.count=filteredCursor.getCount();
                results.values=filteredCursor;
            }
            else if(constraint==null || constraint.length()==0){
                Log.d("me", "performFiltering: length 0 and null i guess!!!");
                results.count=initialCursor.getCount();
                results.values=initialCursor;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCursor=(Cursor)results.values;
            notifyDataSetChanged();
        }
    }

    public void setInitialCursor(Cursor cursor){
        initialCursor=cursor;
    }
}
