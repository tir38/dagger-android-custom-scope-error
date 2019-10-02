package com.example.myapplication.newfeature;

import com.example.myapplication.servicelayer.FeatureManager;

import dagger.Module;
import dagger.Provides;

@Module
public class FeatureModule {

    @Provides
    @FeatureScope
    FeatureManager provideFeatureManager() {
        return new FeatureManager();
    }
}
