package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Steps;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

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

    @Nullable @BindView(R.id.tv_recipe_instruction)
    TextView textView;

    @BindView(R.id.exo_fullscreen_icon)
    ImageView fullscreenIcon;

    @BindView( R.id.tv_no_view_error)
    TextView tvErrorMsg;

    private boolean destroyVideo = true;

    public ItemDetailFragment() {}

    int layout = R.layout.fragment_media_and_recipe_description;
    String screenType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();


//        if ( savedInstanceState != null && savedInstanceState.containsKey( RECIPE_STEPS_KEY) &&
//                savedInstanceState.containsKey( ItemListActivity.SCREEN_TYPE)){
//            mItem = savedInstanceState.getParcelable( RECIPE_STEPS_KEY );
//            screenType = savedInstanceState.getString( ItemListActivity.SCREEN_TYPE );
//        }

        if (getArguments().containsKey(RECIPE_STEPS_KEY) && getArguments().containsKey(ItemListActivity.SCREEN_TYPE)) {

            mItem = getArguments().getParcelable( RECIPE_STEPS_KEY );
            screenType = getArguments().getString( ItemListActivity.SCREEN_TYPE );

            Log.d("Screentype " ,screenType);
        }

        if( screenType.equals( ItemListActivity.TYPE_TWO_PANE )){
            layout =  R.layout.fragment_media_and_recipe_description_horizontal;
        }else {
            layout = R.layout.fragment_media_and_recipe_description;
        }



    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable( RECIPE_STEPS_KEY , mItem);
//        outState.putString( ItemListActivity.SCREEN_TYPE , screenType);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind( this , view);
        if (mItem != null ) {
            previewUrl = mItem.getVideoURL();

            if( screenType.equals( ItemListActivity.TYPE_TWO_PANE )){
                textView.setText( mItem.getShortDescription() );
            }

        }

         return view;
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


    private void initializePlayer() {


        if ( previewUrl != null && playerView !=null && !previewUrl.isEmpty()) {

            Uri uri = Uri.parse(previewUrl);
            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri( context , uri , playerView);
            ExoPlayerVideoHandler.getInstance().goToForeground();

            fullscreenIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    destroyVideo = false;


             }
            });

        }
        else{
            showErrorMsg();
        }
    }

    private void showErrorMsg() {
        tvErrorMsg.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
    }


}
