package com.example.mapdrawingsdk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolygonSender {
    private static PolygonSender instance;
    private final ApiService api;

    private PolygonSender(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://faithful-dolphin-map-sdk-platform-27b4e97b.koyeb.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

    public static void init(String baseUrl) {
        if (instance == null) {
            instance = new PolygonSender(baseUrl);
        }
    }

    public static PolygonSender getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PolygonSender not initialized. Call init() first.");
        }
        return instance;
    }

    public ApiService getApi() {
        return api;
    }
}
