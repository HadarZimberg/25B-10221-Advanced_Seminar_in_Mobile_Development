package com.example.apiserver.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.example.apiserver.model.Polygon;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class PolygonService {

    private static final String COLLECTION_NAME = "polygons";
    private static DatabaseReference databaseReference;

    @PostConstruct
    public void initFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream("firebase/b-10221-seminar-firebase-adminsdk-fbsvc-cc52bf8b32.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://b-10221-seminar-default-rtdb.europe-west1.firebasedatabase.app")
                        .build();

                FirebaseApp.initializeApp(options);
            }

            databaseReference = FirebaseDatabase.getInstance().getReference(COLLECTION_NAME);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase Realtime DB", e);
        }
    }

    public Polygon savePolygon(Polygon polygon) {
        DatabaseReference newRef = databaseReference.push(); // auto-generates ID
        polygon.setId(newRef.getKey());
        newRef.setValueAsync(polygon); // fire-and-forget

        return polygon;
    }

    public List<Polygon> getAllPolygons() {
        List<Polygon> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Polygon polygon = snapshot.getValue(Polygon.class);
                    result.add(polygon);
                }
                latch.countDown();
            }

            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        try {
            latch.await(); // wait for async result
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
