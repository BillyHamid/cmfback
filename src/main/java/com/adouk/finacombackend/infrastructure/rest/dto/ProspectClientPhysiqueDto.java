package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProspectClientPhysiqueDto extends ProspectClientDto {

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
