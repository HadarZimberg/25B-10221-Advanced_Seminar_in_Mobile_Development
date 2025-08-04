package com.example.apiserver.service;

import com.example.apiserver.model.Polygon;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
                        .setCredentials(GoogleCredentials.fromStream(new java.io.FileInputStream(path)))
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
        logger.info("Attempting to save polygon with label: {}", polygon.getLabel());

        Firestore db = getDb();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        polygon.setId(docRef.getId());

        logger.info("Generated ID: {}", polygon.getId());

        ApiFuture<WriteResult> result = docRef.set(polygon);

        try {
            WriteResult writeResult = result.get(10, TimeUnit.SECONDS);
            logger.info("Polygon saved at: {}", writeResult.getUpdateTime());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("❌ Failed to save polygon to Firestore!", e);
            throw new RuntimeException("Failed to save polygon to Firestore", e);
        }

        return polygon;
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

            logger.info("Successfully fetched {} polygons.", polygons.size());
            return polygons;
        } catch (Exception e) {
            logger.error("❌ Failed to load polygons", e);
            throw new RuntimeException("Failed to load polygons", e);
        }
    }
}
