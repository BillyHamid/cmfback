package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.UUID;

@Data
public class UserPasswordUpdateCommand {

    private UUID userId;
    private String oldPassword;
    private String newPassword;
}
