package com.adouk.finacombackend.domain.commands;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.UUID;

@Data
public class UserCommand {
    private String username;
    private String firstname;
    private String lastname;
    private String role;
    private String email;
    private String phone;
    private UUID agenceId;
    @JsonIgnore
    private Client client;

}
