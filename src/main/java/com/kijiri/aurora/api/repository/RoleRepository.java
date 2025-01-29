package com.kijiri.aurora.api.repository;

import com.kijiri.aurora.api.model.Role;
import com.kijiri.aurora.api.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(RoleType name);
}
