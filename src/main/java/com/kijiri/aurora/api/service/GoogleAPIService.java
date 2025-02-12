package com.kijiri.aurora.api.service;

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
        GoogleAccessToken googleAccessToken = fetchAccessToken(authCode, codeVerifier);
        return googleOauth2WebClient.fetchUserDetails(googleAccessToken);
    }

    private GoogleAccessToken fetchAccessToken(String authCode, String codeVerifier) {
        return googleOauth2WebClient.fetchAuthToken(authCode, codeVerifier);
    }
}
