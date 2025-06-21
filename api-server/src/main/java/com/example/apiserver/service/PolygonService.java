package com.example.apiserver.service;

import com.example.apiserver.model.Polygon;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PolygonService {

    private static final String COLLECTION_NAME = "polygons";

    public Polygon savePolygon(Polygon polygon) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("polygons").document(); // auto-generated ID
        polygon.setId(docRef.getId());

        // Save the polygon
        ApiFuture<WriteResult> result = docRef.set(polygon);

        try {
            result.get(); // Wait for success or exception
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to save polygon to Firestore", e);
        }

        return polygon; // So it can be returned in the controller
    }

    public List<Polygon> getAllPolygons() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            List<Polygon> polygons = new ArrayList<>();

            for (QueryDocumentSnapshot doc : documents) {
                System.out.println("Raw Firestore doc: " + doc.getData()); 
                polygons.add(doc.toObject(Polygon.class));
            }

            return polygons;
        } catch (Exception e) {
            System.err.println("ERROR in getAllPolygons:");
            e.printStackTrace();
            throw new RuntimeException("Failed to load polygons", e);
        }
    }
}
