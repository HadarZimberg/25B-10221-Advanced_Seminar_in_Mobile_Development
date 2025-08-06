# 🧪 Example Application

The `:app` module is a ready-to-run implementation using Google Maps.

## Features
- Draw polygons
- Undo last point
- Save with a label
- View saved polygons with auto-zoom
- Markers on each polygon

## Screenshots

![Draw Polygon](assets/screenshots/map_draw.png)

---

## Demo Code Snippet

```java
LatLng telAviv = new LatLng(32.0853, 34.7818);
mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(telAviv, 10));
```
