package com.example.apiserver.util;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleTokenProvider {

	public static String getAccessToken() throws IOException {
        String firebaseConfig = System.getenv("FIREBASE_CONFIG_JSON");

        if (firebaseConfig == null || firebaseConfig.isEmpty()) {
            throw new IOException("FIREBASE_CONFIG_JSON is not set");
        }

        InputStream serviceAccount = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }
}
