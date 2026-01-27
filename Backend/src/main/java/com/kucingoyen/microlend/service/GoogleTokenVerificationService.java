package com.kucingoyen.microlend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenVerificationService {

    private static final Logger log = LoggerFactory.getLogger(GoogleTokenVerificationService.class);

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerificationService(
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken.Payload verify(String tokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(tokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new IllegalArgumentException("Invalid ID token.");
            }
        } catch (GeneralSecurityException | IOException e) {
            log.error("Token verification failed: {}", e.getMessage());
            throw new IllegalArgumentException("Token verification failed", e);
        }
    }
}
