package com.example.apiserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseInitializer.class);

    @PostConstruct
    public void init() {
        logger.info("Initializing Firebase Admin SDK...");
        try {
            String path = System.getenv("FIREBASE_CONFIG_JSON");

            if (path == null) {
                throw new IllegalStateException("❌ FIREBASE_CONFIG_JSON environment variable is not set.");
            }

            File keyFile = new File(path);
            if (!keyFile.exists()) {
                throw new IllegalStateException("❌ Firebase key file not found at path: " + path);
            }

            InputStream serviceAccount = new FileInputStream(keyFile);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("✅ Firebase Admin SDK initialized successfully from {}", path);
            } else {
                logger.info("ℹ️ Firebase Admin SDK already initialized.");
            }

        } catch (Exception e) {
            logger.error("🔥 Firebase initialization failed", e);
            throw new RuntimeException("Failed to initialize Firebase Admin SDK", e);
        }
    }
}
