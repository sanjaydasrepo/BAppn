package com.example.sang.bakingapp;

import android.app.Application;

public class BakingApplication extends Application {
    public static final String PREF_KEY_JSON = "jsonData";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
