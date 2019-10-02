package com.example.myapplication.main;

import com.example.myapplication.MyApplication;
import com.example.myapplication.servicelayer.DataManager;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules =
        {MainModule.class,
                MainActivityBindingModule.class,
                AndroidSupportInjectionModule.class})
public interface MainComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {
        //Required for DaggerApplication injection
    }

    // we need to expose some of the objects in this graph so that other components that depend on MainComponent
    // can get access to them
    DataManager getDataManager();
}