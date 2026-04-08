package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.commands.RoleCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.RoleDto;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    List<Permission> findAll();
}
