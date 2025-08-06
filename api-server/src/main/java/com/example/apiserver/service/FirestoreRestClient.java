package com.example.apiserver.service;

import com.example.apiserver.model.Polygon;
import com.example.apiserver.util.GoogleTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FirestoreRestClient {

	private final GoogleTokenProvider tokenProvider;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${FIREBASE_PROJECT_ID}")
	private String projectId;

	public FirestoreRestClient(GoogleTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	public void savePolygon(Polygon polygon) throws IOException {
		String token = tokenProvider.getAccessToken();

		String url = "https://firestore.googleapis.com/v1/projects/" + projectId + "/databases/(default)/documents/polygons";

		// Convert to Firestore document format
		Map<String, Object> payload = Map.of(
			"fields", Map.of(
				"label", Map.of("stringValue", polygon.getLabel()),
				"points", Map.of("arrayValue", Map.of(
					"values", polygon.getPoints().stream().map(p -> Map.of(
						"mapValue", Map.of("fields", Map.of(
							"lat", Map.of("doubleValue", p.getLat()),
							"lng", Map.of("doubleValue", p.getLng())
						))
					)).toList()
				))
			)
		);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
		restTemplate.postForEntity(url, entity, String.class);
	}
	
	public List<Polygon> getAllPolygons() throws IOException {
	    String token = tokenProvider.getAccessToken();

	    String url = "https://firestore.googleapis.com/v1/projects/" + projectId + "/databases/(default)/documents/polygons";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(token);

	    HttpEntity<Void> entity = new HttpEntity<>(headers);
	    ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

	    List<Polygon> polygons = new ArrayList<>();

	    Object documentsObj = response.getBody().get("documents");
	    if (documentsObj instanceof List<?> documents) {
	        for (Object docObj : documents) {
	            if (!(docObj instanceof Map<?, ?> docMap)) continue;

	            Map<String, Object> fields = (Map<String, Object>) docMap.get("fields");
	            if (fields == null) continue;

	            // Extract label
	            Map<String, Object> labelMap = (Map<String, Object>) fields.get("label");
	            String label = (String) labelMap.get("stringValue");

	            // Extract points array
	            Map<String, Object> pointsMap = (Map<String, Object>) fields.get("points");
	            Map<String, Object> arrayValue = (Map<String, Object>) pointsMap.get("arrayValue");
	            List<Object> values = (List<Object>) arrayValue.get("values");

	            List<com.example.apiserver.model.Point> points = new ArrayList<>();

	            if (values != null) {
	                for (Object valueObj : values) {
	                    Map<String, Object> valueMap = (Map<String, Object>) valueObj;
	                    Map<String, Object> mapValue = (Map<String, Object>) valueMap.get("mapValue");
	                    Map<String, Object> pointFields = (Map<String, Object>) mapValue.get("fields");

	                    Map<String, Object> latMap = (Map<String, Object>) pointFields.get("lat");
	                    Map<String, Object> lngMap = (Map<String, Object>) pointFields.get("lng");

	                    double lat = Double.parseDouble(latMap.get("doubleValue").toString());
	                    double lng = Double.parseDouble(lngMap.get("doubleValue").toString());

	                    points.add(new com.example.apiserver.model.Point(lat, lng));
	                }
	            }

	            Polygon polygon = new Polygon();
	            polygon.setLabel(label);
	            polygon.setPoints(points);

	            polygons.add(polygon);
	        }
	    }

	    return polygons;
	}

	
}
