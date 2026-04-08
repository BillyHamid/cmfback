package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("ASSOCIATION")
@Getter
@Setter
public class ClientAssociation extends Client {

    private String nomOrganisation;
    private String typeOrganisation; // Association, Coopérative, Groupement, etc.
    private String domaineActivite;

    private String nomRepresentant;
    private String prenomRepresentant;
    private String sexeRepresentant;
    private String telRepresentant;
    private String emailRepresentant;
    private String adresseRepresentant;
}
