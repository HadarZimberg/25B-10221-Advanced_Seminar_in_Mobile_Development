package com.example.a25b_10221_advanced_seminar_in_mobile_development;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mapdrawingsdk.MapDrawingSdk;
import com.example.mapdrawingsdk.Point;
import com.example.mapdrawingsdk.Polygon;
import com.example.mapdrawingsdk.PolygonManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final List<LatLng> drawnPoints = new ArrayList<>();
    private com.google.android.gms.maps.model.Polygon currentPolygon;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SDK
        MapDrawingSdk.init("http://192.168.68.101:8080");

        // Create a polygon
        Polygon polygon = new Polygon(
                "Test Polygon from Android",
                Arrays.asList(
                        new Point(32.0853, 34.7818),
                        new Point(32.0855, 34.7822),
                        new Point(32.0851, 34.7825)
                )
        );

        // Send it to the backend
        MapDrawingSdk.sendPolygon(polygon, new PolygonManager.PolygonCallback() {
            @Override
            public void onSuccess() {
                Log.d("SDK", "Polygon sent successfully!");
            }

            @Override
            public void onError(Throwable t) {
                Log.e("SDK", "Failed to send polygon", t);
            }
        });

        // Init map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            fabSave = findViewById(R.id.fab_save);
            fabSave.setOnClickListener(v -> sendPolygonToServer());
            fabLoad = findViewById(R.id.fab_load);
            fabLoad.setOnClickListener(v -> loadPolygonsFromServer());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Map", "Google Map is ready");

        mMap.setOnMapClickListener(latLng -> {
            drawnPoints.add(latLng);
            redrawPolygon();
        });
        LatLng telAviv = new LatLng(32.0853, 34.7818);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 8));
/*
        mMap.setOnMapLongClickListener(latLng -> {
            // Long click = Save polygon
            sendPolygonToServer();
        });*/
    }

    private void loadPolygonsFromServer() {
        MapDrawingSdk.fetchPolygons(polygons -> {
            runOnUiThread(() -> {
                for (Polygon polygon : polygons) {
                    List<LatLng> latLngList = new ArrayList<>();
                    for (Point point : polygon.getPoints()) {
                        latLngList.add(new LatLng(point.getLat(), point.getLng()));
                    }

                    mMap.addPolygon(new PolygonOptions()
                            .addAll(latLngList)
                            .strokeColor(0xFF00AA00)
                            .fillColor(0x3300AA00)
                            .clickable(true));
                }
                Log.d("SDK", "Polygons loaded and displayed.");
            });
        }, throwable -> {
            Log.e("SDK", "Failed to load polygons", throwable);
        });
    }

    private void redrawPolygon() {
        if (currentPolygon != null) {
            currentPolygon.remove();
        }

        if (drawnPoints.size() < 2) return;

        currentPolygon = mMap.addPolygon(new PolygonOptions()
                .addAll(drawnPoints)
                .strokeColor(0xFF0000FF)
                .fillColor(0x330000FF)
        );
    }

    private void sendPolygonToServer() {
        if (drawnPoints.size() < 3) {
            Log.w("SDK", "Polygon must have at least 3 points");
            return;
        }

        // Convert Google LatLng to your SDK's Point
        List<Point> sdkPoints = new ArrayList<>();
        for (com.google.android.gms.maps.model.LatLng latLng : drawnPoints) {
            sdkPoints.add(new Point(latLng.latitude, latLng.longitude));
        }

        Polygon polygon = new Polygon("Polygon at " + System.currentTimeMillis(), sdkPoints);

        MapDrawingSdk.sendPolygon(polygon, new PolygonManager.PolygonCallback() {
            @Override
            public void onSuccess() {
                Log.d("SDK", "Polygon sent successfully!");
            }

            @Override
            public void onError(Throwable t) {
                Log.e("SDK", "Failed to send polygon", t);
            }
        });

        drawnPoints.clear();
        if (currentPolygon != null) currentPolygon.remove();
    }

}