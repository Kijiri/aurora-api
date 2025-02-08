package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.dto.UserDTO;
import com.kijiri.aurora.api.integration.GoogleAPIWebClient;
import com.kijiri.aurora.api.integration.GoogleAccessToken;
import com.kijiri.aurora.api.integration.GoogleUser;
import org.springframework.stereotype.Service;

@Service
public class GoogleAPIService {
    private final GoogleAPIWebClient googleOauth2WebClient;

    public GoogleAPIService(GoogleAPIWebClient googleOauth2WebClient) {
        this.googleOauth2WebClient = googleOauth2WebClient;
    }

    public GoogleUser fetchGoogleUser(String authCode, String codeVerifier) {
        GoogleAccessToken googleAccessToken = verifyToken(authCode, codeVerifier);
        return googleOauth2WebClient.fetchUserDetails(googleAccessToken);
    }

    private GoogleAccessToken verifyToken(String authCode, String codeVerifier) {
        return googleOauth2WebClient.exchangeAuthCodeForToken(authCode, codeVerifier);
    }
}
