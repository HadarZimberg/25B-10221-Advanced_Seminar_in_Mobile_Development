package com.example.apiserver.service;

import com.example.apiserver.model.Polygon;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PolygonService {

    private static final String COLLECTION_NAME = "polygons";
    private static final Logger logger = LoggerFactory.getLogger(PolygonService.class);
    
    public Polygon savePolygon(Polygon polygon) {
        logger.info("Attempting to save polygon with label: {}", polygon.getLabel());

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        polygon.setId(docRef.getId());

        logger.info("Generated ID: {}", polygon.getId());

        ApiFuture<WriteResult> result = docRef.set(polygon);

        try {
            WriteResult writeResult = result.get(10, TimeUnit.SECONDS); // ADD TIMEOUT
            logger.info("Polygon saved at: {}", writeResult.getUpdateTime());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Failed to save polygon to Firestore!", e);
            throw new RuntimeException("Failed to save polygon to Firestore", e);
        }

        return polygon;
    }

    public List<Polygon> getAllPolygons() {
    	logger.info("Fetching all polygons from Firestore...");
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            List<Polygon> polygons = new ArrayList<>();

            for (QueryDocumentSnapshot doc : documents) {
            	logger.debug("Raw Firestore document: {}", doc.getData());
                System.out.println("Raw Firestore doc: " + doc.getData()); 
                polygons.add(doc.toObject(Polygon.class));
            }

            logger.info("Successfully fetched {} polygons.", polygons.size());
            return polygons;
        } catch (Exception e) {
        	logger.error("Failed to load polygons", e);
            System.err.println("ERROR in getAllPolygons:");
            e.printStackTrace();
            throw new RuntimeException("Failed to load polygons", e);
        }
    }
}
