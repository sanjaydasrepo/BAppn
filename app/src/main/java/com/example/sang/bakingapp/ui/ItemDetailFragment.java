package com.example.sang.bakingapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Steps;
import com.example.sang.bakingapp.utils.BakingUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    private static long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;

    private String PLAYBACK_POSITION_KEY = "playbackPosition";
    private Uri uri;
    private BandwidthMeter bandwidthMeter;
    private DefaultDataSourceFactory mediaDataSourceFactory;
    private Timeline.Window window;
    private DefaultTrackSelector trackSelector;
    private Handler mainHandler;


    public ItemDetailFragment() {}

    int layout;
    String screenType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();



        if (getArguments().containsKey(RECIPE_STEPS_KEY)
                && getArguments().containsKey(ItemListActivity.SCREEN_TYPE) ) {

            mItem = getArguments().getParcelable( RECIPE_STEPS_KEY );
            screenType = getArguments().getString( ItemListActivity.SCREEN_TYPE );

        }
        if( screenType.equals( ItemListActivity.TYPE_PORTRAIT )){
            layout =  R.layout.fragment_media_and_recipe_description;
        }else {
            layout = R.layout.fragment_media_and_recipe_description_horizontal;
        }


        if( savedInstanceState != null &&
                savedInstanceState.containsKey(PLAYBACK_POSITION_KEY )){
                 playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);

        }


        bandwidthMeter = new DefaultBandwidthMeter();

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYBACK_POSITION_KEY , playbackPosition);
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
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {


        if( !BakingUtils.isOnline( context )){
            Toast.makeText( context , context.getString(R.string.network_error),Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if(previewUrl == null || previewUrl.isEmpty()){
            showErrorMsg();
            return;
        }


        uri = Uri.parse( previewUrl );

        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


                player = ExoPlayerFactory.newSimpleInstance(context , trackSelector);

                playerView.setPlayer(player);
                player.setPlayWhenReady(playWhenReady);

            boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
                if( haveStartPosition ){
                 player.seekTo( currentWindow , playbackPosition);
                }


        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, !haveStartPosition, true);
        }
        initFullscreenDialog();
        initFullscreenButton();


    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();

        DefaultDataSourceFactory defaultDataSourceFactory =
                new DefaultDataSourceFactory(context , "BakingApp" , bandwidthMeterA);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

         ExtractorMediaSource videoSource = new ExtractorMediaSource( uri , defaultDataSourceFactory , extractorsFactory ,null ,null);
                return videoSource;
    }


    private void showErrorMsg() {
        tvErrorMsg.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
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
}
