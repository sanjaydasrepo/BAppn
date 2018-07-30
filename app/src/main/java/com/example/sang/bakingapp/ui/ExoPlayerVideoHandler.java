package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * Created by bspl-hpl18 on 30/7/18.
 */

public class ExoPlayerVideoHandler {
    private SimpleExoPlayer player;
    private boolean playWhenReady;


    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance(){
        if(instance == null){
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private Uri playerUri;
    private boolean isPlayerPlaying;

    private ExoPlayerVideoHandler(){}

    public void prepareExoPlayerForUri(Context context, Uri uri,
                                       PlayerView playerView){
        if(context != null && uri != null && playerView != null){

                // Create a new player if the player is null or
                // we want to play a new video
                playerUri = uri;

                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(context),
                        new DefaultTrackSelector(), new DefaultLoadControl());



                playerView.setPlayer(player);

                playerView.setControllerHideOnTouch(false);

                boolean playWhenReady = true;
                player.setPlayWhenReady(playWhenReady);



                MediaSource mediaSource = buildMediaSource( playerUri );
                player.prepare(mediaSource, true, false);

            player.clearVideoSurface();
            player.setVideoSurfaceView(
                    (SurfaceView)playerView.getVideoSurfaceView());
            player.seekTo(player.getCurrentPosition() + 1);

        }
    }

    public void releaseVideoPlayer(){
        if (player != null) {

            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
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
    private static MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }


}


