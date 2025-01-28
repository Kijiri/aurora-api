package com.kijiri.aurora.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

    @NotEmpty(message = "Username or email is mandatory")
    @NotBlank(message = "Username or email is mandatory")
    private String login;
    
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    private String password;
}
