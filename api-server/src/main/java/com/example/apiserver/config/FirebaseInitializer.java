package com.example.apiserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseInitializer {

	@PostConstruct
    public void init() {
        try {
            String firebaseConfig = System.getenv("FIREBASE_CONFIG_JSON");
            if (firebaseConfig == null || firebaseConfig.isEmpty()) {
                throw new IllegalStateException("FIREBASE_CONFIG_JSON environment variable is missing.");
            }

            InputStream serviceAccount = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase has been initialized from environment variable!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
