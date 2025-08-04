package com.example.apiserver.config;

import com.example.apiserver.service.PolygonService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class FirebaseInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(PolygonService.class);

    @PostConstruct
    public void init() {
    	logger.info("Initializing Firebase...");
        try {
            System.setProperty("FIRESTORE_TRANSPORT", "rest");

            InputStream serviceAccount;
            String koyebPath = "/app/firebase-key.json";
            File koyebKey = new File(koyebPath);

            if (koyebKey.exists()) {
                System.out.println("Using Firebase key from Koyeb path: " + koyebPath);
                serviceAccount = new FileInputStream(koyebKey);
            } else {
                String localPath = Paths.get("src", "main", "resources", "firebase", "b-10221-seminar-firebase-adminsdk-fbsvc-cc52bf8b32.json").toString();
                File localKey = new File(localPath);
                if (!localKey.exists()) {
                    throw new RuntimeException("❌ Firebase service account file not found in Koyeb or local path.");
                }
                System.out.println("Using local Firebase key: " + localPath);
                serviceAccount = new FileInputStream(localKey);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase has been initialized.");
            }

        } catch (Exception e) {
        	logger.error("Firebase initialization failed:", e);
            e.printStackTrace();
        }
    }
}
