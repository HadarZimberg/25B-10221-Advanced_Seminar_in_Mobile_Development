# 📦 Android SDK Usage

## Initialize
```java
MapDrawingSdk.init("https://your-api.com/api/polygons");
```

## Send a Polygon
```java
MapDrawingSdk.sendPolygon(polygon, new PolygonCallback() {
    public void onSuccess() { ... }
    public void onError(Throwable t) { ... }
});
```

## Fetch Polygons
```java
MapDrawingSdk.fetchPolygons(polygons -> {
    // Show on map
}, throwable -> {
    // Handle error
});
```
