package com.rahulpawar.olaplay.nowplaying;



import android.os.Bundle;

import rx.subscriptions.CompositeSubscription;


public class NowPlayingPresenter {

    private final NowPlayingView view;
    private CompositeSubscription subscriptions;

    public NowPlayingPresenter( NowPlayingView view) {
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }


    public void setData(Bundle b)
    {

    }



    public void onStop() {
        subscriptions.unsubscribe();
    }
}
