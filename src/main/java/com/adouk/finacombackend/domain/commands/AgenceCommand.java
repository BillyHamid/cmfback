package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.UUID;

@Data
public class AgenceCommand {


    private String agencyName;
    private String agencyAdress;
    private String agencyPhone1;
    private String agencyPhone2;
    private String agencyLongitude;
    private String agencyLatitude;
    private UUID gestionnaireId;
}
