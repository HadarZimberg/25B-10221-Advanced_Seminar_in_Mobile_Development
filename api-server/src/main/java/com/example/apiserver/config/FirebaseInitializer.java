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

	        String path = "/app/firebase-key.json";
	        File keyFile = new File(path);

	        if (!keyFile.exists()) {
	            throw new RuntimeException("❌ Firebase key file not found at " + path);
	        }

	        InputStream serviceAccount = new FileInputStream(keyFile);

	        FirebaseOptions options = FirebaseOptions.builder()
	                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                .build();

	        if (FirebaseApp.getApps().isEmpty()) {
	            FirebaseApp.initializeApp(options);
	            logger.info("✅ Firebase has been initialized from /app/firebase-key.json");
	        }
	    } catch (Exception e) {
	        logger.error("🔥 Firebase initialization failed", e);
	        throw new RuntimeException(e);
	    }
	}

}
