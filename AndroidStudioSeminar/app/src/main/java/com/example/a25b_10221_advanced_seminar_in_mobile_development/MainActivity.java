package com.example.a25b_10221_advanced_seminar_in_mobile_development;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLngBounds;
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
    private FloatingActionButton fabUndo;
    private final List<com.google.android.gms.maps.model.Polygon> loadedPolygons = new ArrayList<>();
    private boolean polygonsShown = false;
    private final List<com.google.android.gms.maps.model.Marker> loadedMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            fabSave = findViewById(R.id.fab_save);
            fabSave.setOnClickListener(v -> sendPolygonToServer());
            fabLoad = findViewById(R.id.fab_load);
            fabLoad.setOnClickListener(v -> loadPolygonsFromServer());
            fabUndo = findViewById(R.id.fab_undo);
            fabUndo.setOnClickListener(v -> undoLastPoint());
        }
    }

    private void undoLastPoint() {
        if (!drawnPoints.isEmpty()) {
            drawnPoints.remove(drawnPoints.size() - 1);
            redrawPolygon();
        } else {
            Toast.makeText(this, "No points to undo", Toast.LENGTH_SHORT).show();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 10));
    }

    private void loadPolygonsFromServer() {
        if (polygonsShown) {
            for (com.google.android.gms.maps.model.Polygon p : loadedPolygons) {
                p.remove();
            }
            for (com.google.android.gms.maps.model.Marker m : loadedMarkers) {
                m.remove();
            }
            loadedPolygons.clear();
            loadedMarkers.clear();
            polygonsShown = false;
            Toast.makeText(this, "Polygons hidden", Toast.LENGTH_SHORT).show();
            Log.d("SDK", "Polygons hidden.");
            return;
        }

        MapDrawingSdk.fetchPolygons(polygons -> {
            runOnUiThread(() -> {
                if (polygons.isEmpty()) {
                    Toast.makeText(this, "No polygons found", Toast.LENGTH_SHORT).show();
                    return;
                }

                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                boolean hasPoints = false;

                for (Polygon polygon : polygons) {
                    List<LatLng> latLngList = new ArrayList<>();
                    double sumLat = 0, sumLng = 0;

                    for (Point point : polygon.getPoints()) {
                        LatLng latLng = new LatLng(point.getLat(), point.getLng());
                        latLngList.add(latLng);
                        boundsBuilder.include(latLng);
                        sumLat += point.getLat();
                        sumLng += point.getLng();
                        hasPoints = true;
                    }

                    com.google.android.gms.maps.model.Polygon drawnPolygon = mMap.addPolygon(new PolygonOptions()
                            .addAll(latLngList)
                            .strokeColor(0xFF00AA00)
                            .fillColor(0x3300AA00)
                            .clickable(true));
                    loadedPolygons.add(drawnPolygon);

                    // Calculate center and place a marker with label
                    LatLng center = new LatLng(sumLat / latLngList.size(), sumLng / latLngList.size());
                    com.google.android.gms.maps.model.Marker marker = mMap.addMarker(
                            new com.google.android.gms.maps.model.MarkerOptions()
                                    .position(center)
                                    .title(polygon.getLabel()));
                    loadedMarkers.add(marker);
                }

                polygonsShown = true;
                Toast.makeText(this, loadedPolygons.size() + " polygons displayed", Toast.LENGTH_SHORT).show();
                Log.d("SDK", "Polygons loaded and displayed.");

                if (hasPoints) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));
                }
            });
        }, throwable -> {
            Log.e("SDK", "Failed to load polygons", throwable);
            runOnUiThread(() -> Toast.makeText(this, "Error loading polygons", Toast.LENGTH_SHORT).show());
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
            Toast.makeText(this, "Please draw at least 3 points to create a polygon", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert Google LatLng to SDK's Point
        List<Point> sdkPoints = new ArrayList<>();
        for (com.google.android.gms.maps.model.LatLng latLng : drawnPoints) {
            sdkPoints.add(new Point(latLng.latitude, latLng.longitude));
        }

        Polygon polygon = new Polygon("Polygon at " + System.currentTimeMillis(), sdkPoints);

        MapDrawingSdk.sendPolygon(polygon, new PolygonManager.PolygonCallback() {
            @Override
            public void onSuccess() {
                Log.d("SDK", "Polygon sent successfully!");
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Polygon saved successfully", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(Throwable t) {
                Log.e("SDK", "Failed to send polygon", t);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to save polygon", Toast.LENGTH_SHORT).show());
            }
        });

        drawnPoints.clear();
        if (currentPolygon != null) currentPolygon.remove();
    }

}