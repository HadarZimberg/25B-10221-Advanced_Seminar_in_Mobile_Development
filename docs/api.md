# 🌐 API Service

## POST `/api/polygons`
Create a polygon with a label and list of points.

### Payload
```json
{
  "label": "Polygon A",
  "points": [
    { "lat": 32.0, "lng": 34.8 },
    { "lat": 32.1, "lng": 34.9 }
  ]
}
```

## GET `/api/polygons`
Returns all saved polygons.

---

### 💾 Firebase Integration
- Firestore stores polygons under a `polygons` collection.
- Firestore initialized using Admin SDK and REST transport for compatibility.
