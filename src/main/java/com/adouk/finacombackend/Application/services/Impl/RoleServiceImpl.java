package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.RoleService;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.aggregates.RolePermission;
import com.adouk.finacombackend.domain.commands.RoleCommand;
import com.adouk.finacombackend.infrastructure.repositories.PermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.RolePermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.RoleRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieve all roles with their permissions.
     * @return list of RoleDto
     */
    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> {
            RoleDto roleDto = modelMapper.map(role, RoleDto.class);
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRole(role.getId());
            if (rolePermissions != null) {
                List<Permission> permissions = rolePermissions.stream()
                        .map(RolePermission::getPermission)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                roleDto.setPermissions(permissions);
            }
            return roleDto;
        }).collect(Collectors.toList());
    }

    /**
     * Save a new role.
     * @param roleCommand data for the new role
     * @return created Role
     */
    @Override
    public Role saveRole(RoleCommand roleCommand) {
        Role role = modelMapper.map(roleCommand, Role.class);
        Role savedRole = roleRepository.save(role);
        return assignPermissionsToRole(roleCommand, savedRole);
    }

    /**
     * Update an existing role.
     * @param roleCommand data to update
     * @param id identifier of the role to update
     * @return updated Role
     */
    @Override
    public Role updateRole(RoleCommand roleCommand, UUID id) {
        Role oldRole = roleRepository.findById(id).orElseThrow();
        Role updatedRole = modelMapper.map(roleCommand, Role.class);
        updatedRole.setId(id);
        Role savedRole = roleRepository.save(updatedRole);

        List<RolePermission> existingPermissions = rolePermissionRepository.findByRole(oldRole.getId());
        rolePermissionRepository.deleteAll(existingPermissions);

        return assignPermissionsToRole(roleCommand, savedRole);
    }

    /**
     * Delete a role by its identifier.
     * @param id identifier of the role
     * @return confirmation message
     */
    @Override
    public Map<String, String> delete(UUID id) {
        Role oldRole = roleRepository.findById(id).orElseThrow();
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRole(oldRole.getId());
        rolePermissionRepository.deleteAll(rolePermissions);
        roleRepository.delete(oldRole);
        return Collections.singletonMap("message", "Role deleted");
    }

    private Role assignPermissionsToRole(RoleCommand roleCommand, Role role) {
        if (roleCommand.getPermissionsId() != null) {
            roleCommand.getPermissionsId().forEach(privilegeCode -> {
                Permission permission = permissionRepository.findById(privilegeCode).orElse(null);
                if (permission != null) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRole(role);
                    rolePermission.setPermission(permission);
                    rolePermissionRepository.save(rolePermission);
                }
            });
        }
        return role;
    }
}
