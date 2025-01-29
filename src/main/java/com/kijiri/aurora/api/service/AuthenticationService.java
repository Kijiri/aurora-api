package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.dto.*;
import com.kijiri.aurora.api.model.Role;
import com.kijiri.aurora.api.model.RoleType;
import com.kijiri.aurora.api.model.User;
import com.kijiri.aurora.api.model.UserStatus;
import com.kijiri.aurora.api.repository.RoleRepository;
import com.kijiri.aurora.api.repository.UserRepository;
import com.kijiri.aurora.api.request.AuthenticationRequest;
import com.kijiri.aurora.api.request.RefreshTokenRequest;
import com.kijiri.aurora.api.request.RegistrationRequest;
import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;
import com.kijiri.aurora.api.shared.exception.DuplicateException;
import com.kijiri.aurora.api.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) {
        log.info("Registering user with email: {}", request.getEmail());
        
        validateRegistration(request);

        Role role = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> {
                    log.error("USER role not initialized");
                    return new ResourceNotFoundException(BusinessErrorCodes.RESOURCE_NOT_FOUND, "USER role not initialized");
                });
        
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

    private void validateRegistration(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already taken: {}", request.getEmail());
            throw new DuplicateException(
                    BusinessErrorCodes.EMAIL_TAKEN,
                    String.format("Email is taken for: %s", request.getEmail())
            );
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            log.warn("Username already taken: {}", request.getUserName());
            throw new DuplicateException(
                    BusinessErrorCodes.USERNAME_TAKEN,
                    String.format("Username is taken for: %s", request.getUserName())
            );
        }
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
            
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            //TODO mapper - mapstruct or custom?
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId()).build();

        log.info("Authentication successful for user: {}", request.getLogin());
            
            return AuthenticationResponse.builder()
                    .message("Login successful")
                    .userDTO(userDTO)   
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refreshing token for refreshToken: {}", request.getRefreshToken());
        
        String newAccessToken = jwtService.createNewAccessToken(request.getRefreshToken());

        log.info("New access token generated");
        return new RefreshTokenResponse(newAccessToken);
    }
}
