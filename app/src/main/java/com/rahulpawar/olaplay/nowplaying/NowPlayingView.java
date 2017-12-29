package com.rahulpawar.olaplay.nowplaying;

import com.rahulpawar.olaplay.models.SongData;

import java.util.List;

/**
 * Created by ennur on 6/25/16.
 */
public interface NowPlayingView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

   void onPlayerPause();
   void onPlayerPlay();



    void getSongsListSuccess(List<SongData> songDataList);



}
