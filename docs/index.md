---
title: MapDrawingSDK Documentation
layout: default
---

# MapDrawingSDK

## 🗺️ Map Drawing SDK Documentation

Draw, label, and save polygons from Android to a Firebase‑backed Spring Boot API.

---

## 📚 Documentation Sections

| # | Section | Description |
|---|---------|-------------|
| 🚀 1 | [Getting Started](#1-getting-started) | Setup SDK and backend |
| 🧩 2 | [Integration Examples](#2-integration-examples) | Code samples: draw, save, fetch |
| 🎨 3 | [Customization](#3-customization) | Colors, labels, SDK options |
| 🌐 4 | [API Service Docs](#4-api-service-documentation) | Endpoints & data models |
| 📱 5 | [Example App](#5-example-app) | Android reference implementation |

---

## 🎥 Demo Video

[![Watch the demo video](https://github.com/HadarZimberg/25B-10221-Advanced_Seminar_in_Mobile_Development/blob/main/screenshots/7.JPG)](https://share.synthesia.io/80fc6042-e53e-47ae-8f5b-359d7b27d840)

---

## 1. Getting Started

## Requirements

- Java 17 or newer  
- Android Studio 4.0+ (for the Example App)  
- Google service account JSON with Firestore access  

---

## Backend Setup (API Service)

1. Clone the repo:

    ```bash
    git clone https://github.com/your-username/YourRepo.git
    cd YourRepo/api-server
    ```

2. Set environment variables:

    ```env
    FIREBASE_CONFIG_JSON=<contents of service account JSON>
    FIRESTORE_TRANSPORT=rest
    ```

3. Build and run:

    ```bash
    ./gradlew clean bootRun
    ```

4. Verify service is running at:
    `http://localhost:8080/api/polygons`

---

## Android SDK Setup

1. Add `mapdrawingsdk` module as dependency in your `settings.gradle` and `build.gradle`:

    ```groovy
    include ':mapdrawingsdk'
    implementation project(':mapdrawingsdk')
    ```

2. Initialize the SDK (e.g. in `Application` class):

    ```java
    MapDrawingSdk.init(this, "https://your-koyeb-app.koyeb.app/api/polygons");
    ```

---

## Example App Setup

1. Open the `example-app` folder in Android Studio.
2. Place your `google-services.json` inside the app module root.
3. Build and run on an emulator or device.
4. Use the app interface to:
   - Draw points on the map
   - Use **Undo** to remove the last point
   - Save a polygon via the **Save FAB**
   - Tap **Load FAB** to load polygons from the server

---

## Quick Integration Snippet

```java
MapDrawingSdk.fetchPolygons(polygons -> {
    // show polygons
}, throwable -> {
    // handle error
});
```

---

## More Information

Check out:

- ✅ [API Documentation](api.md)  
- 📦 [SDK Usage Guide](sdk.md)  
- 🧪 [Example App Walkthrough](example.md)  


---

## 2. Integration Examples

Load polygons:
```java
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
```

Save polygons:

```java
private void sendPolygonToServer() {
        if (drawnPoints.size() < 3) {
            Log.w("SDK", "Polygon must have at least 3 points");
            Toast.makeText(this, "Please draw at least 3 points to create a polygon", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show input dialog to enter label
        EditText input = new EditText(this);
        input.setHint("Enter polygon label");

        new AlertDialog.Builder(this)
                .setTitle("Polygon Label")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String label = input.getText().toString().trim();
                    if (label.isEmpty()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String timestamp = sdf.format(new Date());
                        label = "Polygon at " + timestamp;
                    }

                    savePolygonWithLabel(label);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
```
---

## 3. Customization

Customize polygon:

- Stroke/fill colors  
- Label dialog input  
- Error handling and toasts

---

## 4. API Service Documentation

### POST `/api/polygons`

- saves polygon with label and list of lat/lng points.

### GET `/api/polygons`

- returns all polygons in Firestore.

```json
{
  "label": "Zone A",
  "points": [{ "lat": 32.08, "lng": 34.78 }, ...]
}
```

---

## 5. Example App

- Tap to draw polygons  
- Undo last point  
- Save labeled polygon  
- Fetch & display saved polygons  
- Auto‑zoom, marker icons, custom labels

---

## 📄 License

MIT License – see the `LICENSE` file.
