package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.Mandataire;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CompteBancaireDto {
    private UUID id;
    private String numeroCompte;
    private String libelle;
    private String typeCompte;
    private String statut;
    private Client client;
    private String codeBanque;
    private String codeGuichet;
    private String cleRib;
    private String iban;
    private String bic;

    private List<MandataireDto> mandataires = new ArrayList<>();
}
