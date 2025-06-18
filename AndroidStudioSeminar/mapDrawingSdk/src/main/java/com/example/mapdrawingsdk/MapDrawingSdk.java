package com.example.mapdrawingsdk;

import java.util.List;
import java.util.function.Consumer;

public class MapDrawingSdk {

    public static void init(String baseUrl) {
        PolygonSender.init(baseUrl);
    }

    public static void sendPolygon(Polygon polygon, PolygonManager.PolygonCallback callback) {
        PolygonManager.sendPolygon(polygon, callback);
    }

    public static void fetchPolygons(Consumer<List<Polygon>> onResult, Consumer<Throwable> onError) {
        PolygonManager.getPolygons(onResult, onError);
    }
}

