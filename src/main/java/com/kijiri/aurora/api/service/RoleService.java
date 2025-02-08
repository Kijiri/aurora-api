package com.kijiri.aurora.api.service;

import com.kijiri.aurora.api.model.Role;
import com.kijiri.aurora.api.model.RoleType;
import com.kijiri.aurora.api.repository.RoleRepository;
import com.kijiri.aurora.api.shared.enums.BusinessErrorCodes;
import com.kijiri.aurora.api.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(RoleType roleType) {
        return roleRepository.findByName(RoleType.USER)
            .orElseThrow(() -> {
                log.error("{} role not initialized", roleType);
                return new ResourceNotFoundException(BusinessErrorCodes.RESOURCE_NOT_FOUND,
                    String.format("%s role not initialized", roleType.name()));
            });
    }
}
