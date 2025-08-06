# 🔧 Setup Instructions

## Backend (Spring Boot API)

1. Clone the repo and navigate to `api-server/`
2. Place your Firebase service account key at `/api-server/firebase/firebase-key.json`
3. Create `.env` for environment variables:

```env
FIREBASE_CONFIG_JSON=/app/firebase-key.json
FIRESTORE_TRANSPORT=rest
```

4. Run locally:
```bash
./mvnw spring-boot:run
```

---

## Android SDK

1. Add the SDK to your Android app:
```gradle
implementation project(':mapdrawingsdk')
```

2. Initialize in `Application`:
```java
MapDrawingSdk.init("https://your-api.com/api/polygons");
```

---

## Example App

Run the `:app` module to test integration with real map interactions and polygon saving.
