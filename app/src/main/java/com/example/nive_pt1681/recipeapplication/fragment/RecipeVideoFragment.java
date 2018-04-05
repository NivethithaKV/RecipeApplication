package com.example.nive_pt1681.recipeapplication.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;

/**
 * Created by nive-pt1681 on 20/03/18.
 */

public class RecipeVideoFragment extends Fragment {

    private VideoView mVideo;
    private MediaController mMediaController;
    private int position;
    private static String recipeId;
    private TextView noVideoWarning;

    public static Fragment newInstance(String recipe_id){
        recipeId=recipe_id;
        return new RecipeVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recipe_video_layout,container,false);
        mVideo=view.findViewById(R.id.recipe_video);
        noVideoWarning=view.findViewById(R.id.no_video_warning_text);

        Log.d("me", "onCreateView: Video Fragment");

        Cursor cursor=getActivity().getContentResolver().query(RecipeEntry.CONTENT_URI,new String[]{RecipeEntry.RECIPE_ID,RecipeEntry.RECIPE_VIDEO},RecipeEntry.RECIPE_ID+"=?",new String[]{recipeId},null);

        if(cursor!=null && cursor.moveToFirst()){
            String videoUri=cursor.getString(cursor.getColumnIndex(RecipeEntry.RECIPE_VIDEO));
            if(videoUri!=null){
                Uri uri=Uri.parse(videoUri);

                mVideo.setVideoURI(uri);

                mMediaController=new MediaController(getActivity());
                mMediaController.setAnchorView(mVideo);
                mVideo.setMediaController(mMediaController);
                mMediaController.setVisibility(View.INVISIBLE);
                noVideoWarning.setVisibility(View.GONE);

                mVideo.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent motionEvent)
                    {
                        if (mVideo.isPlaying())
                        {
                            mVideo.pause();
                            position = mVideo.getCurrentPosition();
                            return false;
                        }
                        else
                        {
                            mVideo.seekTo(position);
                            mVideo.start();
                            return false;
                        }
                    }
                });
            }
            else{
                mVideo.setVisibility(View.GONE);
                noVideoWarning.setText(R.string.no_video_warning);
            }
        }

        return view;
    }
}
