package com.example.apiserver.util;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

@Component
public class GoogleTokenProvider {

    public String getAccessToken(String firebaseServiceJson) throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(firebaseServiceJson.getBytes()))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/datastore"));
        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }
}
