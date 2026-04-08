package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleCommand {
    private String label;
    private String code;
    private String description;
    private List<UUID> permissionsId;
}
