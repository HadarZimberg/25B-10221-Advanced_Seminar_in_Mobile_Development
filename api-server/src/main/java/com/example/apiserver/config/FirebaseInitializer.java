package com.example.apiserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
public class FirebaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseInitializer.class);

    @PostConstruct
    public void init() {
        logger.info("Initializing Firebase...");

        try {
            System.setProperty("FIRESTORE_TRANSPORT", "rest");

            String filePath = "/app/b-10221-seminar-firebase-adminsdk-fbsvc-955d54a49d.json";
            File keyFile = new File(filePath);
            if (!keyFile.exists()) {
                throw new RuntimeException("❌ Firebase key file not found at: " + filePath);
            }

            InputStream serviceAccount = new FileInputStream(keyFile);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("✅ Firebase initialized");
            }

        } catch (Exception e) {
            logger.error("🔥 Firebase initialization failed", e);
            throw new RuntimeException("Firebase initialization error", e);
        }
    }
}
