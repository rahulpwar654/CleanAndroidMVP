package com.rahulpawar.olaplay.home;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.rahulpawar.olaplay.BaseApp;
import com.rahulpawar.olaplay.R;

import com.rahulpawar.olaplay.models.SongData;
import com.rahulpawar.olaplay.models.SongsViewModel;
import com.rahulpawar.olaplay.networking.Service;
import com.rahulpawar.olaplay.roomdb.AppDatabase;
import com.rahulpawar.olaplay.roomdb.RoomDB;
import com.rahulpawar.olaplay.roomdb.SongsDao;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends BaseApp implements HomeView,SwipeRefreshLayout.OnRefreshListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 100;
    private RecyclerView list;
    @Inject
    public Service service;
    ProgressBar progressBar;
    AppDatabase appDatabase;
    HomePresenter presenter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SongsAdapter adapter;
    private ImageView mSearchIcon, mEditTextClear;
    private EditText mSearchName;
    SongsDao songsDao;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);

        renderView();
        init();

        presenter = new HomePresenter(service, this);
        //presenter.getCityList();
        presenter.getSongList();

    }

    public void renderView() {
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        list = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        mSearchName = (EditText) findViewById(R.id.edittext_company);
        mSearchIcon = (ImageView) findViewById(R.id.magnify_icon);
        mEditTextClear = (ImageView) findViewById(R.id.magnify_cross);
        progressBar = findViewById(R.id.progress);



        mEditTextClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchName.setText("");
                mEditTextClear.setVisibility(View.GONE);
            }
        });


        mSearchName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mSearchIcon.setVisibility(View.GONE);
                mEditTextClear.setVisibility(View.VISIBLE);
                if (s.length() == 0) {
                    mEditTextClear.setVisibility(View.GONE);
                    mSearchIcon.setVisibility(View.VISIBLE);
                }
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                   // mListEmployDetail = adapter.mHolidayListFilter;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
        });
    }

    public void init() {
        list.setLayoutManager(new LinearLayoutManager(this));

        appDatabase = RoomDB.getInstance(this);//Room.databaseBuilder(NowPlayingActivity.this, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
        songsDao = appDatabase.songsDao();


        SongsViewModel viewModel = ViewModelProviders.of(this).get(SongsViewModel.class);
        viewModel.init(songsDao);
        checkPermissionAndThenLoad();
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
    public void getSongsListSuccess(List<SongData> songDataList) {
        insertUser(songDataList);

        mSwipeRefreshLayout.setRefreshing(false);
        adapter = new SongsAdapter(getApplicationContext(), songDataList);

        list.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        if (presenter != null)
            presenter.getSongList();
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

        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            }
        }
    }

    public void insertUser( final List<SongData> songDataList) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.songsDao().nukeTable();
                appDatabase.songsDao().insertAll(songDataList);
                return null;
            }
        }.execute();
    }



    //Local braodcastReceiver to communicate with Music Service
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