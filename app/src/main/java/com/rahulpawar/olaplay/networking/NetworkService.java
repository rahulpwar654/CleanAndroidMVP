package com.rahulpawar.olaplay.networking;


import com.rahulpawar.olaplay.models.CityListResponse;
import com.rahulpawar.olaplay.models.SongData;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface NetworkService {

    @GET("v1/city")
    Observable<CityListResponse> getCityList();

    @GET("studio")
    Observable<List<SongData>> getSongsList();


}
