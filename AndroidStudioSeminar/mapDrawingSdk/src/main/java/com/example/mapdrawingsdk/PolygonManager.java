package com.example.mapdrawingsdk;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolygonManager {

    public interface PolygonCallback  {
        void onSuccess();
        void onError(Throwable t);
    }

    public static void sendPolygon(Polygon polygon, PolygonCallback callback) {
        PolygonSender.getInstance().getApi().sendPolygon(polygon).enqueue(new retrofit2.Callback<Polygon>() {
            @Override
            public void onResponse(Call<Polygon> call, Response<Polygon> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(new Exception("Server error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Polygon> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public static void getPolygons(Consumer<List<Polygon>> onResult, Consumer<Throwable> onError) {
        PolygonSender.getInstance().getApi().getPolygons().enqueue(new Callback<List<Polygon>>() {
            @Override
            public void onResponse(Call<List<Polygon>> call, Response<List<Polygon>> response) {
                if (response.isSuccessful()) {
                    onResult.accept(response.body());
                } else {
                    onError.accept(new Exception("Server error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Polygon>> call, Throwable t) {
                onError.accept(t);
            }
        });
    }
}

