package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MandataireDto {

    private UUID id;

    private String nom;

    private String prenom;

    private String lienParente;

    private String telephone;

    private String email;

    private List<PieceJustificativeDto> piecesJustificatives;
}
