package com.example.nive_pt1681.recipeapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.NotificationEntry;

/**
 * Created by nive-pt1681 on 23/03/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>{

    private Cursor mCursor;
    private Context mContext;

    public NotificationAdapter(Cursor cursor, Context context){
        mCursor=cursor;
        mContext=context;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.single_notification_item,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        if(mCursor.moveToPosition(position)){
            String message=mCursor.getString(mCursor.getColumnIndex(NotificationEntry.NOTIFICATION_MESSAGE));
            int type=mCursor.getInt(mCursor.getColumnIndex(NotificationEntry.NOTIFICATION_CATEGORY));
            holder.bind(message,type);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null){
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor newCursor){
        if(newCursor==mCursor){
            return;
        }
        mCursor=newCursor;
        if(mCursor!=null){
            notifyDataSetChanged();
        }
        else{
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        if(mCursor!=null && mCursor.moveToPosition(position)){
            return mCursor.getInt(position);
        }
        return 0;
    }


    public class NotificationHolder extends RecyclerView.ViewHolder{
        public TextView notificationMessage;
        public ImageView notificationTypeImage;

        public NotificationHolder(View itemView) {
            super(itemView);
            notificationMessage=itemView.findViewById(R.id.notification_message);
            notificationTypeImage=itemView.findViewById(R.id.notification_type);
        }

        public void bind(String message,int notificationType){
            notificationMessage.setText(message);
            notificationTypeImage.setImageResource(R.drawable.ic_favorites);
        }
    }
}
