package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Mandataire {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nom;

    private String prenom;

    private String lienParente;

    private String telephone;

    private String email;

    @ManyToOne
    private CompteBancaire compteBancaire;

}

