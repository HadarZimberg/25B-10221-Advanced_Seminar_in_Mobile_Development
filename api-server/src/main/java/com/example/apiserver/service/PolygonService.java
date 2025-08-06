package com.example.apiserver.service;

import com.example.apiserver.model.Polygon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolygonService {

	private static final Logger logger = LoggerFactory.getLogger(PolygonService.class);
	private final FirestoreRestClient firestoreRestClient;

    public PolygonService(FirestoreRestClient firestoreRestClient) {
		this.firestoreRestClient = firestoreRestClient;
	}

	public Polygon savePolygon(Polygon polygon) {
		try {
			firestoreRestClient.savePolygon(polygon);
			logger.info("Polygon saved via REST");
			return polygon;
		} catch (Exception e) {
			logger.error("Failed to save polygon via REST", e);
			throw new RuntimeException("Failed to save polygon via REST", e);
		}
	}

	public List<Polygon> getAllPolygons() {
		logger.info("Fetching all polygons via REST...");
		try {
			List<Polygon> polygons = firestoreRestClient.getAllPolygons();
			logger.info("Successfully fetched {} polygons.", polygons.size());
			return polygons;
		} catch (Exception e) {
			logger.error("Failed to fetch polygons via REST", e);
			throw new RuntimeException("Failed to fetch polygons via REST", e);
		}
	}

}
