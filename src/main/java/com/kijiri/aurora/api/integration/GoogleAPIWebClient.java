package com.kijiri.aurora.api.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleAPIWebClient {
    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String oauthUrl;
    private final String openIdUrl;

    public GoogleAPIWebClient(@Value("${aurora.api.google.oauth-url}")
                              String oauthUrl,
                              @Value("${aurora.api.google.client-id}")
                              String clientId,
                              @Value("${aurora.api.google.redirect-uri}")
                              String redirectUri,
                              @Value("${aurora.api.google.client-secret}")
                              String clientSecret,
                              @Value("${aurora.api.google.openid-url}") String openIdUrl) {
        this.webClient = WebClient.builder()
            .build();
        this.oauthUrl = oauthUrl;
        this.openIdUrl = openIdUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public GoogleAccessToken exchangeAuthCodeForToken(String authCode, String codeVerifier) {

        return webClient.post()
            .uri(oauthUrl + "token")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(buildGoogleTokenRequest(authCode, codeVerifier))
            .retrieve()
            .bodyToMono(GoogleAccessToken.class)
            .block();
    }

    public GoogleUser fetchUserDetails(GoogleAccessToken googleAccessToken) {
        return webClient.get()
            .uri(openIdUrl + "userinfo")
            .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", googleAccessToken.getAccess_token()))
            .retrieve()
            .bodyToMono(GoogleUser.class)
            .block();
    }

    private BodyInserters.FormInserter<String> buildGoogleTokenRequest(String authCode, String codeVerifier) {
        return BodyInserters.fromFormData("code", authCode)
            .with("client_id", clientId)
            .with("client_secret", clientSecret)
            .with("redirect_uri", redirectUri)
            .with("grant_type", "authorization_code")
            .with("code_verifier", codeVerifier);
    }
}
