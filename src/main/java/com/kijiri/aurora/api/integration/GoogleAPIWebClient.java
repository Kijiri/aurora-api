package com.kijiri.aurora.api.integration;

import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;
import com.kijiri.aurora.api.shared.exception.BadRequestException;
import com.kijiri.aurora.api.shared.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class GoogleAPIWebClient {
    private final WebClient googleOauthWebClient;
    private final WebClient googleOpenIDWebClient;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleAPIWebClient(@Value("${aurora.api.google.oauth-url}")
                              String oauthUrl,
                              @Value("${aurora.api.google.client-id}")
                              String clientId,
                              @Value("${aurora.api.google.redirect-uri}")
                              String redirectUri,
                              @Value("${aurora.api.google.client-secret}")
                              String clientSecret,
                              @Value("${aurora.api.google.openid-url}") String openIdUrl) {
        this.googleOauthWebClient = WebClient.builder()
            .baseUrl(oauthUrl)
            .build();
        this.googleOpenIDWebClient = WebClient.builder()
            .baseUrl(openIdUrl)
            .build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    @Retryable(retryFor = ServiceException.class, backoff = @Backoff(5000))
    public GoogleAccessToken fetchAuthToken(String authCode, String codeVerifier) {
        try {
            return googleOauthWebClient.post()
                .uri(uriBuilder -> uriBuilder.pathSegment("token").build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(buildGoogleTokenRequest(authCode, codeVerifier))
                .retrieve()
                .bodyToMono(GoogleAccessToken.class)
                .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new ServiceException(BusinessErrorCodes.GOOGLE_API_SERVER_ERROR,
                    String.format("Encountered server error: %s, statusCode: %s", e.getMessage(), e.getStatusCode()));
            }
            throw new BadRequestException(BusinessErrorCodes.GOOGLE_API_4XX,
                String.format("The request was unprocessable due to: %s, statusCode: %s",
                    e.getMessage(),
                    e.getStatusCode()));
        }
    }

    @Retryable(retryFor = ServiceException.class, maxAttempts = 2, backoff = @Backoff(5000))
    public GoogleUser fetchUserDetails(GoogleAccessToken googleAccessToken) {
        try {
            return googleOpenIDWebClient.get()
                .uri(uriBuilder -> uriBuilder.pathSegment("userinfo").build())
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", googleAccessToken.getAccess_token()))
                .retrieve()
                .bodyToMono(GoogleUser.class)
                .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new ServiceException(BusinessErrorCodes.GOOGLE_API_SERVER_ERROR,
                    String.format("Encountered server error: %s, statusCode: %s", e.getMessage(), e.getStatusCode()));
            }
            throw new BadRequestException(BusinessErrorCodes.GOOGLE_API_4XX,
                String.format("The request was unprocessable due to: %s, statusCode: %s",
                    e.getMessage(),
                    e.getStatusCode()));
        }
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
