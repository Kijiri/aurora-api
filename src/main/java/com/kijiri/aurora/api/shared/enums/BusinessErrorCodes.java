package com.kijiri.aurora.api.shared.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    USERNAME_TAKEN(1001, HttpStatus.CONFLICT, "Username is taken"),
    EMAIL_TAKEN(1002, HttpStatus.CONFLICT, "Email is taken"),
    USER_NOT_FOUND(1003, HttpStatus.NOT_FOUND, "No account found with that email or username"),


    RESOURCE_NOT_FOUND(1404, HttpStatus.NOT_FOUND, "Resource not found");


    private final int code;
    private final HttpStatus httpStatus;
    private final String description;


    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
