package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.commands.RoleCommand;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.domain.commands.UserPasswordUpdateCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.RoleDto;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RoleService {

    /**
     * Retrieve all roles with their permissions.
     * @return list of RoleDto
     */
    List<RoleDto> getAllRoles();

    /**
     * Save a new role.
     * @param roleCommand data for the new role
     * @return created Role
     */
    Role saveRole(RoleCommand roleCommand);

    /**
     * Update an existing role.
     * @param roleCommand data to update
     * @param id identifier of the role to update
     * @return updated Role
     */
    Role updateRole(RoleCommand roleCommand, UUID id);

    /**
     * Delete a role by its identifier.
     * @param id identifier of the role
     * @return confirmation message
     */
    Map<String, String> delete(UUID id);
}
