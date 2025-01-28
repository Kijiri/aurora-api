package com.kijiri.aurora.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
}
