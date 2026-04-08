package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.UUID;

@Data
public class ValidationDossierCommand {

    private UUID dossierUuid;
    private UUID agentUuid;
    private String commentaire;
    private String idSib;
}
