package com.example.myapplication.main;

import com.example.myapplication.servicelayer.DataManager;

import dagger.Binds;
import dagger.Module;

@Module()
public abstract class MainModule {

    @Binds
    abstract DataManager bindsDataManager(DataManager dataManager);

    // IF I CHANGE TO providing instead of binding then everything works fine

//    @Provides
//    @Singleton
//    static DataManager provideDataManager() {
//        return new DataManager();
//    }

}
