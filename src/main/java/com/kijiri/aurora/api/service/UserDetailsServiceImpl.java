package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.model.User;
import com.kijiri.aurora.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.findByEmail(login);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }
        
        Optional<User> userByUsername = userRepository.findByUserName(login);
        if (userByUsername.isPresent()) {
            return userByUsername.get();
        }
        
        //TODO exception handling
        throw new UsernameNotFoundException("User not found with email or username: " + login);
    }
}
