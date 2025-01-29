package com.kijiri.aurora.api.repository;

import com.kijiri.aurora.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    Optional<User> findByEmailOrUserName(String email, String userName);
}
