package com.rahulpawar.olaplay.player;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.rahulpawar.olaplay.Constants;
import com.rahulpawar.olaplay.models.SongData;

import java.io.IOException;

/**
 * Created by warlord on 12/20/2017.
 */

public class MusicPlayerService extends Service implements ExtractorMediaSource.EventListener
{



   public static SimpleExoPlayer player;
   public static boolean isPlaying=false;
    int mPlaybackState=0;


    public static String NowPlaying="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // Log.d("service", "onCreate");


        try {
            player =getExoPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "onStartCommand");

        try {

            int action =intent.getIntExtra("ACTION",0);
            String source="";
            if(action<3) {
                source = intent.getStringExtra(Constants.song);
                String nowPlaying = intent.getStringExtra(Constants.Action);
                if(action==Constants.MUSIC_PLAY)
                {
                    //check is it new play or Resume playing
                    if(NowPlaying.equalsIgnoreCase(nowPlaying)){
                        action=Constants.MUSIC_RESUME;
                    }

                }
            }
            performMusicActions(action,source);
           // player.prepare();
            //player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }

    public void performMusicActions(int action,String source)
    {
        switch (action)
        {
            case Constants.MUSIC_PLAY:


                String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";
                Uri uri = Uri.parse(source);
                DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                        userAgent, null,
                        DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                        DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                        true);
                MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, Mp3Extractor.FACTORY,null,
                         null);

                getExoPlayer().prepare(mediaSource);
                getExoPlayer().setPlayWhenReady(true);
                getExoPlayer().getPlaybackState();
                isPlaying=true;
                break;

            case Constants.MUSIC_PAUSE:
                if(isPlaying)
                {
                    player.setPlayWhenReady(false);
                    mPlaybackState= player.getPlaybackState();
                    isPlaying=false;
                }
                break;


            case Constants.MUSIC_RESUME:
                if(!isPlaying)
                {
                    player.setPlayWhenReady(true);
                    mPlaybackState= player.getPlaybackState();
                   //player.setPlaybackParameters(new PlaybackParameters());
                    isPlaying=true;
                }

                break;

            case Constants.MUSIC_NEXT:

                break;
            case Constants.MUSIC_STOP:
                if(player!=null)
                {
                    player.stop();
                    player.release();

                }

                break;

        }


    }





    public  SimpleExoPlayer getExoPlayer()
    {


        // 1. Create a default TrackSelector
        //Handler mainHandler = new Handler();
        if (player == null) {


           // BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            //TrackSelection.Factory videoTrackSelectionFactory =new AdaptiveTrackSelection.Factory(bandwidthMeter);

            TrackSelector trackSelector =   new DefaultTrackSelector( new DefaultBandwidthMeter());
            player = ExoPlayerFactory.newSimpleInstance(this,trackSelector);


        }
        return player;
    }


    @Override
    public void onLoadError(IOException error) {

    }
}
