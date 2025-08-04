package com.example.a25b_10221_advanced_seminar_in_mobile_development;

import android.app.Application;

import com.example.mapdrawingsdk.MapDrawingSdk;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize SDK with the base URL
        MapDrawingSdk.init("https://faithful-dolphin-map-sdk-platform.koyeb.app");
    }
}
