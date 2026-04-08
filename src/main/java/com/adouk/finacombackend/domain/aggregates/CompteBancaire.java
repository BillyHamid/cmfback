package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class CompteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String numeroCompte;

    private String libelle;

    private String typeCompte;

    private String statut;

    @ManyToOne
    private Client client;

    private String codeBanque;
    private String codeGuichet;
    private String cleRib;
    private String iban;
    private String bic;

}

