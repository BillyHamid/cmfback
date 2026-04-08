package com.adouk.finacombackend.domain.commands;

import lombok.Data;

@Data
public class AuthCommand {

    private String username;
    private String password;
}
