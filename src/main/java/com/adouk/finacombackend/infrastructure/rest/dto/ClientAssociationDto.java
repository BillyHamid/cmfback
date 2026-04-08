package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientAssociationDto extends ClientDto {

    private String nomOrganisation;
    private String typeOrganisation;
    private String domaineActivite;

    private String nomRepresentant;
    private String prenomRepresentant;
    private String sexeRepresentant;
    private String telRepresentant;
    private String emailRepresentant;
    private String adresseRepresentant;

    private List<PieceJustificativeDto> piecesJustificatives;
}

