package com.coderdhruv.stopwatch;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
}
