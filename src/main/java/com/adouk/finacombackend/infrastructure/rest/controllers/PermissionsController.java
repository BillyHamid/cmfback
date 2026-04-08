package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.PermissionService;
import com.adouk.finacombackend.Application.services.Interfaces.RoleService;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.commands.RoleCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.RoleDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permissions")
@AllArgsConstructor
public class PermissionsController {

    private final PermissionService permissionService;

    @GetMapping("")
    public ResponseEntity<List<Permission>> findAll() {
        return ResponseEntity.ok(permissionService.findAll());
    }
}
