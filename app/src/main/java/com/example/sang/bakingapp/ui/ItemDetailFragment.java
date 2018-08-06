package com.example.sang.bakingapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Steps;
import com.example.sang.bakingapp.utils.BakingUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
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

    @Nullable @BindView(R.id.tv_recipe_instruction)
    TextView textView;

    @BindView(R.id.exo_fullscreen_icon)
    ImageView fullscreenIcon;

    @BindView( R.id.tv_no_view_error)
    TextView tvErrorMsg;




    private boolean destroyVideo = true;
    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen;
    private FrameLayout fullscreenButton;
    private SimpleExoPlayer player;

    public ItemDetailFragment() {}

    int layout;
    String screenType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();


        if (getArguments().containsKey(RECIPE_STEPS_KEY)
                && getArguments().containsKey(ItemListActivity.SCREEN_TYPE)) {

            mItem = getArguments().getParcelable( RECIPE_STEPS_KEY );
            screenType = getArguments().getString( ItemListActivity.SCREEN_TYPE );

        }

        if( screenType.equals( ItemListActivity.TYPE_PORTRAIT )){
            layout =  R.layout.fragment_media_and_recipe_description;
        }else {
            layout = R.layout.fragment_media_and_recipe_description_horizontal;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(layout, container, false);
        ButterKnife.bind( this , view);
        if (mItem != null ) {
            previewUrl = mItem.getVideoURL();

            if( screenType.equals( ItemListActivity.TYPE_PORTRAIT )){
                textView.setText( mItem.getShortDescription() );
            }
         }

         return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(destroyVideo){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    private void initializePlayer() {

       if( !BakingUtils.isOnline( context )){
           Toast.makeText( context , context.getString(R.string.network_error),Toast.LENGTH_LONG)
                   .show();
           return;
       }

        if ( previewUrl != null && playerView !=null && !previewUrl.isEmpty()) {

            Uri uri = Uri.parse(previewUrl);

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri( context , uri , playerView ,player);
            ExoPlayerVideoHandler.getInstance().goToForeground();

            initFullscreenDialog();
            initFullscreenButton();

        }
        else{
            showErrorMsg();
        }
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(context,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }
    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullscreenIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }
    private void closeFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) getActivity().findViewById(R.id.main_media_frame)).addView(playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        fullscreenIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_expand));
    }
    private void initFullscreenButton() {

        PlayerControlView controlView = playerView.findViewById(R.id.exo_controller);
        fullscreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    private void showErrorMsg() {
        tvErrorMsg.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
    }


}
