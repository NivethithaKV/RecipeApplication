package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReportsEntry;
import com.example.nive_pt1681.recipeapplication.model.Report;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 16/03/18.
 */

public class ReportsAdapter extends  RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>{

    private Cursor mCursor;
    private Context mContext;
    private int id;

    public ReportsAdapter(Cursor cursor, Context context){
        mCursor=cursor;
        mContext=context;
    }


    public void swapCursor(Cursor newCursor){
        if(newCursor==mCursor){
            return;
        }
        mCursor=newCursor;
        if(mCursor!=null){
            id=newCursor.getColumnIndex(RecipeContract.RecipeEntry._ID);
            notifyDataSetChanged();
        }
        else{
            id=-1;
            notifyDataSetChanged();
        }
    }

    @Override
    public ReportsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.single_report_item,parent,false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportsViewHolder holder, int position) {
        if(mCursor.moveToPosition(position)){

            Report report=new Report();
            report.setRecipeId(mCursor.getString(mCursor.getColumnIndex(ReportsEntry.REPORTS_RECIPE_ID)));
            report.setReportId(UUID.fromString(mCursor.getString(mCursor.getColumnIndex(ReportsEntry.REPORTS_ID))));
            report.setReportedDate(Long.parseLong(mCursor.getString(mCursor.getColumnIndex(ReportsEntry.REPORTS_DATE))));
            report.setReportedRegarding(mCursor.getString(mCursor.getColumnIndex(ReportsEntry.REPORTS_REGARDING)));
            report.setReportedProblem(mCursor.getString(mCursor.getColumnIndex(ReportsEntry.REPORTS_PROBLEM)));
            report.setVulnerability(mCursor.getInt(mCursor.getColumnIndex(ReportsEntry.REPORTS_VULNERABILITY)));
            Cursor cursor=null;
            try{
                cursor=mContext.getContentResolver().query(ImageEntry.CONTENT_URI,null, ImageEntry.IMAGES_REPORT_ID+"=?",new String[]{report.getReportId().toString()}, ImageEntry._ID);
                if(cursor!=null && cursor.moveToFirst()){
                    report.setReportImage(cursor.getString(cursor.getColumnIndex(ImageEntry.IMAGES_URI)));
                }
                String user_id=mCursor.getString(mCursor.getColumnIndex(ReportsEntry.REPORTS_USER_ID));
                cursor=mContext.getContentResolver().query(RecipeContract.UserEntry.CONTENT_URI,new String[]{RecipeContract.UserEntry.USER_NAME, RecipeContract.UserEntry.USER_ID, RecipeContract.UserEntry.USER_IMAGE}, RecipeContract.UserEntry.USER_ID+"=?",new String[]{user_id},null);
                if(cursor!=null && cursor.moveToFirst()){
                    report.setReportedUserName(cursor.getString(cursor.getColumnIndex(RecipeContract.UserEntry.USER_NAME)));
                    report.setReportedUserImage(cursor.getString(cursor.getColumnIndex(RecipeContract.UserEntry.USER_IMAGE)));
                }
            }
            finally {
                cursor.close();
            }
            holder.bind(report);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(id);
        }
        return 0;
    }


    public class ReportsViewHolder extends RecyclerView.ViewHolder{
        public TextView reportedUserName,reportedRegarding,reportedProblem,reportedDate;
        public RatingBar reportedVulnerability;
        public ImageView reportedUserImage;

        public ReportsViewHolder(View itemView) {
            super(itemView);
            reportedUserName=itemView.findViewById(R.id.reported_user_name);
            reportedRegarding=itemView.findViewById(R.id.reported_regarding_item);
            reportedProblem=itemView.findViewById(R.id.reported_problem);
            reportedUserImage=itemView.findViewById(R.id.reported_user_image);
            reportedDate=itemView.findViewById(R.id.reported_date);
            reportedVulnerability=itemView.findViewById(R.id.reported_vulnerability);
        }

        public void bind(Report report){
            reportedUserName.setText(report.getReportedUserName());
            reportedRegarding.setText(report.getReportedRegarding());
            reportedProblem.setText(report.getReportedProblem());
            reportedVulnerability.setRating(report.getVulnerability());
            reportedDate.setText(DateFormat.format(" hh:mm:ss dd/MM/yyyy",new Date(report.getReportedDate())));
            if(report.getReportedUserImage()!=null)
                Glide.with(mContext)
                        .load(report.getReportedUserImage())
                        .into(reportedUserImage);
            else
                reportedUserImage.setImageResource(R.drawable.default_person);
        }
    }

}