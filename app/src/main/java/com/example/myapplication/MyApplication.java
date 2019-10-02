package com.example.myapplication;

import com.example.myapplication.main.DaggerMainComponent;
import com.example.myapplication.main.MainComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MyApplication extends DaggerApplication {

    // TODO hacky way to expose and make a singleton for FeatureComponent; don't do this
    public static MainComponent component;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        if (component == null) {
            component = (MainComponent) DaggerMainComponent.builder().create(this);
        }

        return component;
    }
}
