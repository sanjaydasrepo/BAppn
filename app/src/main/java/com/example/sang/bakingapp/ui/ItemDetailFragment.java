package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Steps;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    public static final String RECIPE_STEPS_KEY = "recipe_steps_key";

    private Steps mItem;
    private String previewUrl;
    private Context context;

    @BindView(R.id.audio_view)
    PlayerView playerView;

    @BindView(R.id.tv_recipe_instruction)
    TextView textView;

    @BindView(R.id.exo_fullscreen_icon)
    ImageView fullscreenIcon;

    private boolean destroyVideo = true;

    public ItemDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();



        if (getArguments().containsKey(RECIPE_STEPS_KEY)) {

            mItem = getArguments().getParcelable( RECIPE_STEPS_KEY );
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_and_recipe_description, container, false);
        ButterKnife.bind( this , view);
        if (mItem != null) {

            previewUrl = mItem.getVideoURL();
            textView.setText( mItem.getShortDescription() );
         }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

            //initializePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();

            initializePlayer();


    }
    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();

            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(destroyVideo){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            textView.setVisibility( View.GONE);
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=300;
            textView.setVisibility( View.VISIBLE);

            playerView.setLayoutParams(params);
        }
    }

    private void initializePlayer() {


        if ( previewUrl != null && playerView !=null ) {

            Uri uri = Uri.parse(previewUrl);
            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri( context , uri , playerView);
            ExoPlayerVideoHandler.getInstance().goToForeground();

            fullscreenIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    destroyVideo = false;
                    Log.d("Tag","Click");
                }
            });

        }
    }




}
