package com.kijiri.aurora.api.controller;

import com.kijiri.aurora.api.dto.AuthenticationRequest;
import com.kijiri.aurora.api.dto.AuthenticationResponse;
import com.kijiri.aurora.api.dto.RegistrationRequest;
import com.kijiri.aurora.api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
//        return ResponseEntity.ok(authenticationService(request));
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String refreshToken) {
        return null;
//        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
    }
}
