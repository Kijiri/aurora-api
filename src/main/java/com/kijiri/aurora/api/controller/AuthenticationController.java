package com.kijiri.aurora.api.controller;

import com.kijiri.aurora.api.controller.request.AuthenticationRequest;
import com.kijiri.aurora.api.controller.request.Oauth2Request;
import com.kijiri.aurora.api.controller.request.RefreshTokenRequest;
import com.kijiri.aurora.api.controller.request.RegistrationRequest;
import com.kijiri.aurora.api.dto.AuthenticationResponse;
import com.kijiri.aurora.api.dto.RefreshTokenResponse;
import com.kijiri.aurora.api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequest request) {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @PostMapping("/authenticate/google")
    public ResponseEntity<AuthenticationResponse> googleAuthenticate(@RequestBody Oauth2Request oauth2Request) {
        return ResponseEntity.ok(authenticationService.authenticateByGoogle(oauth2Request));
    }
}
