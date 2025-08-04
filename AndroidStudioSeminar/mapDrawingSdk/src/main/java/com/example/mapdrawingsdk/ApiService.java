package com.example.mapdrawingsdk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {

    @POST("polygons")
    Call<Polygon> sendPolygon(@Body Polygon polygon);

    @GET("polygons")
    Call<List<Polygon>> getPolygons();
}
