package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProspectClientMoralDto extends ProspectClientDto {
    private String raisonSociale;
    private String rccm;
    private String numIfu;
    private String formeJuridique;
    private String domaineActivite;

    private String nomResponsable;
    private String prenomResponsable;
    private String sexeResponsable;
    private String telResponsable;
    private String emailResponsable;
    private List<PieceJustificativeDto> piecesJustificatives;

}
