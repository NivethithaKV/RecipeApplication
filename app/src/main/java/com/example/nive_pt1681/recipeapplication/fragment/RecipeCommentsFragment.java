package com.example.nive_pt1681.recipeapplication.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.activity.HomeActivity;
import com.example.nive_pt1681.recipeapplication.adapter.RecipeCommentsAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.CommentsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;

import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nive-pt1681 on 21/03/18.
 */

public class RecipeCommentsFragment extends Fragment {

    private static String recipeId;
    private RecyclerView mCommentRecyclerView;
    private RecipeCommentsAdapter mAdapter;
    private EditText commentedText;
    private ImageButton attachImage;
    private ImageButton postCommentButton;
    public static final int RESULT_LOAD_IMAGE=0;

    public static Fragment newInstance(String recipe_id){
        recipeId=recipe_id;
        return new RecipeCommentsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recipe_detail_comments,container,false);
        mCommentRecyclerView=view.findViewById(R.id.comments_list);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new RecipeCommentsAdapter(null,getActivity());
        mCommentRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(), CommentsEntry.CONTENT_URI,null, CommentsEntry.COMMENTS_RECIPE_ID+"=?",new String[]{recipeId}, CommentsEntry.COMMENTS_DATE+" ASC");
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
        commentedText=view.findViewById(R.id.write_comment);
        attachImage=view.findViewById(R.id.add_comment_image);
        postCommentButton=view.findViewById(R.id.send_comment_button);

        attachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMAGE);
            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=null;
                try{
                    cursor=getActivity().getContentResolver().query(CommentsEntry.CONTENT_URI,new String[]{"MAX("+CommentsEntry._ID+") AS MAX_ID"},null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        int count=cursor.getInt(0);
                        ContentValues contentValues=new ContentValues();
                        contentValues.put(CommentsEntry._ID,count+1);
                        contentValues.put(CommentsEntry.COMMENTS_COMMENT,commentedText.getText().toString());
                        contentValues.put(CommentsEntry.COMMENTS_ID,UUID.randomUUID().toString());
                        contentValues.put(CommentsEntry.COMMENTS_RECIPE_ID,recipeId);
                        contentValues.put(CommentsEntry.COMMENTS_DATE,String.valueOf(new Date().getTime()));
                        contentValues.put(CommentsEntry.COMMENTS_USER_ID, HomeActivity.id);
                        Uri uri=getActivity().getContentResolver().insert(CommentsEntry.CONTENT_URI,contentValues);
                        if(uri!=null){
                            Toast.makeText(getActivity(),"Inserted Successfully...",Toast.LENGTH_SHORT).show();
                            commentedText.setText("");

                            ContentValues values=new ContentValues();
                            cursor=getActivity().getContentResolver().query(RecipeContract.NotificationEntry.CONTENT_URI,new String[]{"MAX(" + RecipeContract.FavoritesEntry._ID + ") AS max_id"}, null, null, null);
                            if(cursor!=null && cursor.moveToFirst()){
                                count=cursor.getInt(0);
                                values.put(RecipeContract.NotificationEntry._ID,count+1);
                                values.put(RecipeContract.NotificationEntry.NOTIFICATION_CATEGORY, RecipeContract.NotificationEntry.CATEGORY_COMMENTS);
                                values.put(RecipeContract.NotificationEntry.NOTIFICATION_ID, UUID.randomUUID().toString());
                                values.put(RecipeContract.NotificationEntry.NOTIFICATION_RECIPE_ID,recipeId);
                                cursor=getActivity().getContentResolver().query(RecipeEntry.CONTENT_URI,new String[]{RecipeEntry.RECIPE_TITLE,RecipeEntry.RECIPE_BY_USER_ID},RecipeEntry.RECIPE_ID+"=?",new String[]{recipeId},null);
                                cursor.moveToFirst();
                                values.put(RecipeContract.NotificationEntry.NOTIFICATION_MESSAGE,"Someone Commented in your recipe "+cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_TITLE)));
                                values.put(RecipeContract.NotificationEntry.NOTIFICATION_TO_USER_ID,cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_BY_USER_ID)));
                                getActivity().getContentResolver().insert(RecipeContract.NotificationEntry.CONTENT_URI,values);
                            }
                        }
                    }
                }
                finally {
                    cursor.close();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            if (selectedImg != null) {
                ContentValues contentValues=new ContentValues();
                String comments_id=UUID.randomUUID().toString();
                Cursor cursor=null;
                try{
                    cursor=getActivity().getContentResolver().query(ImageEntry.CONTENT_URI,new String[]{"MAX("+ImageEntry._ID+") AS MAX_ID"},null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        int count=cursor.getInt(0);
                        contentValues.put(ImageEntry._ID,count+1);
                        contentValues.put(ImageEntry.IMAGES_RECIPE_ID,recipeId);
                        contentValues.put(ImageEntry.IMAGES_ID, UUID.randomUUID().toString());
                        contentValues.put(ImageEntry.IMAGES_URI,selectedImg.toString());
                        contentValues.put(ImageEntry.IMAGES_CATEGORY,ImageEntry.CATEGORY_COMMENTS);
                        contentValues.put(ImageEntry.IMAGES_COMMENT_ID,comments_id);

                        Uri uri=getActivity().getContentResolver().insert(ImageEntry.CONTENT_URI,contentValues);
                        cursor=getActivity().getContentResolver().query(CommentsEntry.CONTENT_URI,new String[]{"MAX("+CommentsEntry._ID+") AS MAX_ID"},null,null,null);
                        if(cursor!=null && cursor.moveToFirst()){
                            count=cursor.getInt(0);
                            contentValues=new ContentValues();
                            contentValues.put(CommentsEntry._ID,count+1);
                            contentValues.put(CommentsEntry.COMMENTS_COMMENT,"#image#");
                            contentValues.put(CommentsEntry.COMMENTS_ID,comments_id);
                            contentValues.put(CommentsEntry.COMMENTS_RECIPE_ID,recipeId);
                            contentValues.put(CommentsEntry.COMMENTS_DATE,String.valueOf(new Date().getTime()));
                            contentValues.put(CommentsEntry.COMMENTS_USER_ID, HomeActivity.id);
                            uri=getActivity().getContentResolver().insert(CommentsEntry.CONTENT_URI,contentValues);
                        }
                        if(uri!=null){
                            Toast.makeText(getContext(),"Image inserted successfully...",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
                finally {
                    cursor.close();
                }
            }
        }
    }

}
