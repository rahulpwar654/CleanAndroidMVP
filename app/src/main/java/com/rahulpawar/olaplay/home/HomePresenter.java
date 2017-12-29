package com.rahulpawar.olaplay.home;



import com.rahulpawar.olaplay.models.SongData;
import com.rahulpawar.olaplay.networking.NetworkError;
import com.rahulpawar.olaplay.networking.Service;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class HomePresenter {
    private final Service service;
    private final HomeView view;
    private CompositeSubscription subscriptions;

    public HomePresenter(Service service, HomeView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }



    public void getSongList() {
        view.showWait();

        Subscription subscription = service.getSongsList(new Service.GetSongsListCallback() {
            @Override
            public void onSuccess(List<SongData> songDataList) {
                view.removeWait();
                view.getSongsListSuccess(songDataList);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }


    public void onStop() {
        subscriptions.unsubscribe();
    }
}
