package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Agent;
import com.adouk.finacombackend.domain.aggregates.ClientStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ClientPhysiquemobileDto {

    private UUID id;
    private String adresse;
    private String email;
    private String telephone1;
    private String telephone2;
    private String bp;
    private String mentionParticuliere;
    private ClientStatus kycStatus;
    private Boolean kycValidated = false;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String profession;
    private String employeur;
    private String adresseEmployeur;
    private String numeroCNIB;

    private List<PieceJustificativeDto> piecesJustificatives;

}
