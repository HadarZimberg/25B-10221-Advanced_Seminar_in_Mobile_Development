package com.example.mapdrawingsdk;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolygonSender {
    private static PolygonSender instance;
    private final ApiService api;

    private PolygonSender(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://faithful-dolphin-map-sdk-platform-2.koyeb.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
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
