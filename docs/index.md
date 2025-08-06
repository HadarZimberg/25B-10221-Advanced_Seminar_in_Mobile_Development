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

- Clone the repo  
- Open `:app` in Android Studio  
- Add `google-services.json`  
- Add SDK dependency:
```gradle
implementation project(":mapdrawingsdk")
```

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
