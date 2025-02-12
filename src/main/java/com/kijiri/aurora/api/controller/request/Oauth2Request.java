package com.kijiri.aurora.api.controller.request;

import lombok.Getter;

@Getter
public class Oauth2Request {
    private String authCode;
    private String codeVerifier;
}
