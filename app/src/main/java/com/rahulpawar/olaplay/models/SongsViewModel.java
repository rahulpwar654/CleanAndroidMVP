package com.rahulpawar.olaplay.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.rahulpawar.olaplay.roomdb.SongsDao;

/**
 * Created by brijesh on 18/9/17.
 */

public class SongsViewModel extends ViewModel {

    public LiveData<PagedList<SongData>> userList;

    public SongsViewModel() {

    }

    public void init(SongsDao songsDao) {
        userList = songsDao.songsDataBySong().create(0,
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPageSize(10)
                        .setPrefetchDistance(5)
                        .build());
    }
}
