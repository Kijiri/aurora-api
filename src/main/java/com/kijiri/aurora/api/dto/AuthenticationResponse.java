package com.kijiri.aurora.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private String message;
    private UserDTO userDTO;
    private String accessToken;
    private String refreshToken;
}
