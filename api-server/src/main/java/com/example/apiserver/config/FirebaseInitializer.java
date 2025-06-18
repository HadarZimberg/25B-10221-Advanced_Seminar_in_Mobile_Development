package com.example.apiserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount =
            		new FileInputStream("src/main/resources/firebase/b-10221-seminar-firebase-adminsdk-fbsvc-d68c2d2361.json");
            /*InputStream serviceAccount = getClass().getClassLoader()
            	    .getResourceAsStream("firebase/b-10221-seminar-firebase-adminsdk-fbsvc-d68c2d2361.json");*/
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase has been initialized!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
