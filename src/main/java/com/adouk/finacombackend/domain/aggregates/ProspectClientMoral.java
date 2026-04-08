package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("MORAL")
@Getter
@Setter
public class ProspectClientMoral extends ProspectClient {

    private String raisonSociale;
    private String rccm;
    private String numIfu;
    private String formeJuridique;
    private String domaineActivite;

    // Responsable légal
    private String nomResponsable;
    private String prenomResponsable;
    private String sexeResponsable;
    private String telResponsable;
    private String emailResponsable;
    private String adresseResponsable;
}
