package com.example.myapplication.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Dagger Module just for {@link android.app.Activity} bindings
 */
@Module
public abstract class MainActivityBindingModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();
}
