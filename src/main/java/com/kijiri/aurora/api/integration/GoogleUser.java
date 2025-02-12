package com.kijiri.aurora.api.integration;

import lombok.Getter;

@Getter
public class GoogleUser {
    private String sub;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
    private Boolean email_verified;
}
