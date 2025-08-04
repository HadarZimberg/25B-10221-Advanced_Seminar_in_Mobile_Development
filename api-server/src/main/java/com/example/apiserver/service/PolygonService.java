package com.example.apiserver.service;

import com.example.apiserver.model.Polygon;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PolygonService {

    private static final String COLLECTION_NAME = "polygons";
    private static final Logger logger = LoggerFactory.getLogger(PolygonService.class);
    private static Firestore firestoreInstance;

    private Firestore getDb() {
        if (firestoreInstance == null) {
            try {
                logger.info("Initializing Firestore manually with REST transport...");

                String path = System.getenv("FIREBASE_CONFIG_JSON");
                if (path == null) {
                    throw new IllegalStateException("FIREBASE_CONFIG_JSON environment variable not set");
                }

                firestoreInstance = FirestoreOptions.newBuilder()
                        .setCredentials(GoogleCredentials.fromStream(new FileInputStream(path)))
                        .setChannelProvider(InstantiatingHttpJsonChannelProvider.newBuilder().build())
                        .build()
                        .getService();

                logger.info("✅ Firestore (REST) initialized successfully.");
            } catch (Exception e) {
                logger.error("🔥 Failed to initialize Firestore with REST transport", e);
                throw new RuntimeException("Firestore initialization error", e);
            }
        }
        return firestoreInstance;
    }

    public Polygon savePolygon(Polygon polygon) {
        try {
            Firestore db = getDb();
            DocumentReference docRef = db.collection(COLLECTION_NAME).document();
            polygon.setId(docRef.getId()); // Store generated ID
            ApiFuture<WriteResult> result = docRef.set(polygon);
            result.get(); // wait for success
            logger.info("✅ Polygon saved with ID: {}", polygon.getId());
            return polygon;
        } catch (Exception e) {
            logger.error("❌ Failed to save polygon", e);
            throw new RuntimeException("Failed to save polygon to Firestore", e);
        }
    }

    public List<Polygon> getAllPolygons() {
        logger.info("Fetching all polygons from Firestore...");
        try {
            Firestore db = getDb();
            ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            List<Polygon> polygons = new ArrayList<>();

            for (QueryDocumentSnapshot doc : documents) {
                logger.debug("Raw Firestore document: {}", doc.getData());
                polygons.add(doc.toObject(Polygon.class));
            }

            logger.info("✅ Successfully fetched {} polygons.", polygons.size());
            return polygons;
        } catch (Exception e) {
            logger.error("❌ Failed to load polygons", e);
            throw new RuntimeException("Failed to load polygons", e);
        }
    }
}
