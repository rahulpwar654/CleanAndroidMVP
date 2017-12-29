package com.rahulpawar.olaplay.nowplaying;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.rahulpawar.olaplay.BaseApp;
import com.rahulpawar.olaplay.R;
import com.rahulpawar.olaplay.models.SongData;
import com.rahulpawar.olaplay.player.MusicPlayerService;

import java.util.List;

public class NowPlayingActivity extends BaseApp implements NowPlayingView {

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 100;

    ProgressBar progressBar;

    NowPlayingPresenter presenter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    SimpleExoPlayerView simpleExoPlayerView;
    PlaybackControlView playbackControlView;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDeps().inject(this);
        setContentView(R.layout.activity_nowplaying);

        renderView();
        init();

        presenter = new NowPlayingPresenter( this);



    }

    public void renderView() {

        simpleExoPlayerView = findViewById(R.id.exoplayer_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Ola Play");
        }
    }

    public void init() {

        simpleExoPlayerView.setPlayer(MusicPlayerService.player);

        //checkPermissionAndThenLoad();
    }


    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String appErrorMessage) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPlayerPause() {

    }

    @Override
    public void onPlayerPlay() {

    }


    @Override
    public void getSongsListSuccess(List<SongData> songDataList) {


    }



    public void onRefresh() {

    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //good to go

                } else {

                    checkPermissionAndThenLoad();
                }
                return;
            }


        }
    }

    private void checkPermissionAndThenLoad() {


        if (ContextCompat.checkSelfPermission(NowPlayingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(NowPlayingActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(NowPlayingActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            }
        }
    }

/*    public void insertUser(final AppDatabase appDatabase) {
        final DatabaseCreator databaseCreator = new DatabaseCreator();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().insertAll(databaseCreator.getRandomUserList());
                return null;
            }
        }.execute();
    }*/


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Double currentSpeed = intent.getDoubleExtra("currentSpeed", 20);
            Double currentLatitude = intent.getDoubleExtra("latitude", 0);
            Double currentLongitude = intent.getDoubleExtra("longitude", 0);
            //  ... react to local broadcast message


        }};
}