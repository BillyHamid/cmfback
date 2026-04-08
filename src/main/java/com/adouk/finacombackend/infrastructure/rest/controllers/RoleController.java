package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.RoleService;
import com.adouk.finacombackend.Application.services.Interfaces.UserSessionQueryService;
import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.commands.RoleCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.RoleDto;
import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public List<RoleDto> findAll() {
        return roleService.getAllRoles();
    }

    @PostMapping("")
    public Role save(@RequestBody RoleCommand roleCommand) {
        return roleService.saveRole(roleCommand);
    }

    @PatchMapping("/update/{id}")
    public Role update(@RequestBody RoleCommand roleCommand, @PathVariable String id) {
        return roleService.updateRole(roleCommand, UUID.fromString(id));
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, String> delete(@PathVariable String id) {
        return roleService.delete(UUID.fromString(id));
    }

}
