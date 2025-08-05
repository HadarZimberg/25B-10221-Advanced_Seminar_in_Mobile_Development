package com.example.apiserver.util;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

@Component
public class GoogleTokenProvider {

	public String getAccessToken() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
            .createScoped("https://www.googleapis.com/auth/cloud-platform");

        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }
}
