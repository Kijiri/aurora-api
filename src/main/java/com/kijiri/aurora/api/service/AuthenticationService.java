package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.dto.RegistrationRequest;
import com.kijiri.aurora.api.model.Role;
import com.kijiri.aurora.api.model.User;
import com.kijiri.aurora.api.model.UserStatus;
import com.kijiri.aurora.api.repository.RoleRepository;
import com.kijiri.aurora.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void register(RegistrationRequest request) {
        Role role = roleRepository.findByName("USER")
                //TODO better exception handling
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
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
    }
}
