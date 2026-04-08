package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.PermissionService;
import com.adouk.finacombackend.Application.services.Interfaces.RoleService;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.commands.RoleCommand;
import com.adouk.finacombackend.infrastructure.repositories.PermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.RoleRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.RoleDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
}
