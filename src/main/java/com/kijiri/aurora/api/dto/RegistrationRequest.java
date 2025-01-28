package com.kijiri.aurora.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "First name is required")
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "Username is required")
    @NotEmpty(message = "Username cannot be empty")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
