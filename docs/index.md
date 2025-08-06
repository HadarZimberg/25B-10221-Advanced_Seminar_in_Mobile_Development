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

> *(Replace this with your own video)*

<video controls width="100%">
  <source src="assets/demo.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

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

```java
MapDrawingSdk.init(context, "https://your-api/api/polygons");
sdk.startDrawing();
sdk.savePolygon("Zone A");
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
