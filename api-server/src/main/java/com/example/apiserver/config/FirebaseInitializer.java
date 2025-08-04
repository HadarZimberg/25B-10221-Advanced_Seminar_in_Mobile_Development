package com.example.apiserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
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
                System.out.println("Firebase has been initialized.");
            }

        } catch (Exception e) {
            System.err.println("Firebase initialization failed:");
            e.printStackTrace();
        }
    }
}
