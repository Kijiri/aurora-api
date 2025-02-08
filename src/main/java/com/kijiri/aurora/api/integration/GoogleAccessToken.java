package com.kijiri.aurora.api.integration;

import lombok.Getter;

@Getter
public class GoogleAccessToken {
    private String access_token;
    private long expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;
    private String id_token;
}
