package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    public static final String RECIPE_STEPS_KEY = "recipe_steps_key";
    /**
     * The dummy content this fragment is presenting.
     */
    private Steps mItem;

    TextView textView;
    private SimpleExoPlayer player;

    @BindView(R.id.audio_view)
    PlayerView playerView;
    private int currentWindow;
    private boolean playWhenReady;
    private long playbackPosition;


    private String previewUrl;
    private Context context;

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
            ((TextView) view.findViewById(R.id.tv_recipe_instruction)).setText(mItem.getShortDescription());


            textView = view.findViewById(R.id.tv_recipe_instruction);
            previewUrl = mItem.getVideoURL();
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

        if ( previewUrl != null && playerView !=null ) {
            Uri uri = Uri.parse(previewUrl);
            

        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getString(R.string.exoplayer_codelab))).
                createMediaSource(uri);
    }
    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }


    public class ExoPlayerVideoHandler
    {
        private ExoPlayerVideoHandler instance;

        public ExoPlayerVideoHandler getInstance(){
            if(instance == null){
                instance = new ExoPlayerVideoHandler();
            }
            return instance;
        }

        private Uri playerUri;
        private boolean isPlayerPlaying;

        private ExoPlayerVideoHandler(){}

        public void prepareExoPlayerForUri(Context context, Uri uri,
                                           PlayerView exoPlayerView){
            if(context != null && uri != null && exoPlayerView != null){
                if(!uri.equals(playerUri) || player == null){
                    // Create a new player if the player is null or
                    // we want to play a new video
                    playerUri = uri;

                    player = ExoPlayerFactory.newSimpleInstance(
                            new DefaultRenderersFactory(context),
                            new DefaultTrackSelector(), new DefaultLoadControl());



                    playerView.setPlayer(player);

                    playerView.setControllerHideOnTouch(false);

                    boolean playWhenReady = false;
                    player.setPlayWhenReady(playWhenReady);
                    int currentWindow = 0;
                    int playbackPosition = 0;
                    player.seekTo(currentWindow, playbackPosition);


                    // Do all the standard ExoPlayer code here...
                    MediaSource mediaSource = buildMediaSource( playerUri );
                    player.prepare(mediaSource, true, false);
                }
                player.clearVideoSurface();
                player.setVideoSurfaceView(
                        (SurfaceView)exoPlayerView.getVideoSurfaceView());
                player.seekTo(player.getCurrentPosition() + 1);
                exoPlayerView.setPlayer(player);
            }
        }

        public void releaseVideoPlayer(){
            if(player != null)
            {
                player.release();
            }
            player = null;
        }

        public void goToBackground(){
            if(player != null){
                isPlayerPlaying = player.getPlayWhenReady();
                player.setPlayWhenReady(false);
            }
        }

        public void goToForeground(){
            if(player != null){
                player.setPlayWhenReady(isPlayerPlaying);
            }
        }
    }

}
