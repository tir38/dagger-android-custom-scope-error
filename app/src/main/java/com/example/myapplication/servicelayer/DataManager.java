package com.example.myapplication.servicelayer;

import javax.inject.Singleton;

@Singleton
public class DataManager {

    public String getData() {
        return "DI is working";
    }
}
