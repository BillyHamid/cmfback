package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PHYSIQUE")
@Getter
@Setter
public class ProspectClientPhysique extends ProspectClient {

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String profession;
    private String employeur;
    private String adresseEmployeur;
    private String numeroCNIB;
}
