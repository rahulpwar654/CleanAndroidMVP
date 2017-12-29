package com.rahulpawar.olaplay.networking;


import com.rahulpawar.olaplay.models.CityListResponse;
import com.rahulpawar.olaplay.models.SongData;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }




    public Subscription getSongsList(final GetSongsListCallback callback) {

        return networkService.getSongsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<SongData>>>() {
                    @Override
                    public Observable<? extends List<SongData>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<SongData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(List<SongData> songDataList) {
                        callback.onSuccess(songDataList);

                    }
                });
    }


    public interface GetSongsListCallback{
        void onSuccess(List<SongData> songDataList);

        void onError(NetworkError networkError);
    }




}
