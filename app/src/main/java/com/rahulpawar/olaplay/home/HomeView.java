package com.rahulpawar.olaplay.home;

import com.rahulpawar.olaplay.models.SongData;

import java.util.List;

/**
 * Created by ennur on 6/25/16.
 */
public interface HomeView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    //void getCityListSuccess(CityListResponse cityListResponse);


    void getSongsListSuccess(List<SongData> songDataList);



}
