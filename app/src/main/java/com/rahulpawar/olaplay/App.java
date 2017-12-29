package com.rahulpawar.olaplay;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.rahulpawar.olaplay.roomdb.RoomDB;

/**
 * Created by warlord on 12/17/2017.
 */

public class App extends Application {

    private boolean appRunning = false;
    private static SimpleExoPlayer player;
   private static App app;


    public static  App getInstance()
    {
       return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        RoomDB.getInstance(this); //This will provide AppDatabase Instance
        //app= (App) getApplicationContext();
    }

    public  ExoPlayer getExoPlayer()
    {


        // 1. Create a default TrackSelector
        //Handler mainHandler = new Handler();
        if (player == null) {


        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        TrackSelector trackSelector =   new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),trackSelector);
        }
        return player;
    }


    /*public  void addPlayer(String url)
    {
        try {
            //   private void initMediaPlayer() {
            Handler mHandler = new Handler();

            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";
            Uri uri = Uri.parse(url);
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                    userAgent, null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true);
            MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, Mp3Extractor.FACTORY,
                    mHandler, null);

            //TrackSelector trackSelector = new DefaultTrackSelector(TrackSelection.Factory);
            // DefaultLoadControl loadControl = new DefaultLoadControl();
            // ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            //exoPlayer.addListener(this);

            //exoPlayer.prepare(mediaSource);
            //exoPlayer.setPlayWhenReady(true);


            getExoPlayer().prepare(mediaSource);
            getExoPlayer().setPlayWhenReady(true);
        }catch (Exception e){e.printStackTrace();}
    //}
    }*/







}
