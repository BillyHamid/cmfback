package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

@Data
public class GestionnaireDto {

    private String firstname;
    private String lastname;
    private String phone;
    private String email;

    private String agencyName;
    private String agencyAdress;
    private String agencyPhone1;
    private String agencyPhone2;
    private String agencyLongitude;
    private String agencyLatitude;
}
