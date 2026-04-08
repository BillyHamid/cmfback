package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class RoleDto {

    private UUID id;
    private String label;
    private String code;
    private String description;
    private List<Permission> permissions;
}
