package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.controller.request.AuthenticationRequest;
import com.kijiri.aurora.api.controller.request.Oauth2Request;
import com.kijiri.aurora.api.controller.request.RefreshTokenRequest;
import com.kijiri.aurora.api.controller.request.RegistrationRequest;
import com.kijiri.aurora.api.dto.AuthenticationResponse;
import com.kijiri.aurora.api.dto.RefreshTokenResponse;
import com.kijiri.aurora.api.dto.UserDTO;
import com.kijiri.aurora.api.integration.GoogleUser;
import com.kijiri.aurora.api.model.*;
import com.kijiri.aurora.api.repository.UserRepository;
import com.kijiri.aurora.api.shared.exception.DuplicateException;
import com.kijiri.aurora.api.shared.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kijiri.aurora.api.shared.enums.BusinessErrorCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final GoogleAPIService googleAPIService;

    public void register(RegistrationRequest request) {
        log.info("Registering user with email: {}", request.getEmail());

        validateRegistration(request);

        Role role = roleService.findRoleByName(RoleType.USER);

        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .userName(request.getUserName())
            .email(request.getEmail())
            .hashedPassword(passwordEncoder.encode(request.getPassword()))
            //TODO email validation from unverified to verified?
            .status(UserStatus.VERIFIED)
            .roles(List.of(role))
            .build();

        userRepository.save(user);
        log.info("User registered successfully with email: {}", request.getEmail());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with login: {}", request.getLogin());

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
            )
        );

        User user = (User) auth.getPrincipal();
        AuthenticationResponse authenticationResponse = getAuthenticationResponse(user);
        log.info("Authentication successful for user: {}", request.getLogin());
        return authenticationResponse;
    }

    public AuthenticationResponse authenticateByGoogle(Oauth2Request oauth2Request) {
        GoogleUser googleUser = googleAPIService.fetchGoogleUser(oauth2Request.getAuthCode(),
            oauth2Request.getCodeVerifier());
        if (!googleUser.getEmail_verified()) {
            throw new ForbiddenException(EMAIL_UNVERIFIED, "User has unverified Gmail");
        }
        User user = userRepository.findByEmail(googleUser.getEmail())
            .orElseGet(() -> registerGoogleUser(googleUser));

        return getAuthenticationResponse(user);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        log.debug("Refreshing token for refreshToken: {}", request.getRefreshToken());

        String newAccessToken = jwtService.createNewAccessToken(request.getRefreshToken());

        log.debug("New access token generated");
        return new RefreshTokenResponse(newAccessToken);
    }

    private void validateRegistration(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already taken: {}", request.getEmail());
            throw new DuplicateException(
                EMAIL_TAKEN,
                String.format("Email is taken for: %s", request.getEmail())
            );
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            log.warn("Username already taken: {}", request.getUserName());
            throw new DuplicateException(
                USERNAME_TAKEN,
                String.format("Username is taken for: %s", request.getUserName())
            );
        }
    }

    private User registerGoogleUser(GoogleUser googleUser) {
        Role role = roleService.findRoleByName(RoleType.USER);
        User user = User.builder()
            .firstName(googleUser.getGiven_name())
            .lastName(googleUser.getFamily_name())
            .email(googleUser.getEmail())
            .status(UserStatus.VERIFIED)
            .roles(List.of(role))
            .loginType(LoginType.GOOGLE)
            .build();
        return userRepository.save(user);
    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserDTO userDTO = UserDTO.builder()
            .id(user.getId()).build();

        return AuthenticationResponse.builder()
            .message("Login successful")
            .userDTO(userDTO)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
