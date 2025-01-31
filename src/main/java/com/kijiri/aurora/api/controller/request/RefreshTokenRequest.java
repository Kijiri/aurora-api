package com.kijiri.aurora.api.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private String refreshToken;
}
