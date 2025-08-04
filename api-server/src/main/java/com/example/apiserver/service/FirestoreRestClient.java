package com.example.apiserver.service;

import com.example.apiserver.model.Point;
import com.example.apiserver.model.Polygon;
import com.example.apiserver.util.GoogleTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class FirestoreRestClient {

    @Value("${FIREBASE_CONFIG_JSON}")
    private String firebaseConfigJson;

    private final GoogleTokenProvider tokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String projectId = "b-10221-seminar";

    public FirestoreRestClient(GoogleTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public void savePolygon(Polygon polygon) throws IOException {
        String token = tokenProvider.getAccessToken(firebaseConfigJson);
        String url = "https://firestore.googleapis.com/v1/projects/" + projectId + "/databases/(default)/documents/polygons";

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
}
