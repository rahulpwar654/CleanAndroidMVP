package com.rahulpawar.olaplay.deps;


import com.rahulpawar.olaplay.home.HomeActivity;
import com.rahulpawar.olaplay.networking.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ennur on 6/28/16.
 */
@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {
    void inject(HomeActivity homeActivity);
}
