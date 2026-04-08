package com.adouk.finacombackend.domain.commands;

import com.adouk.finacombackend.domain.aggregates.Client;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CompteBancaireCommand {

    private String numeroCompte;
    private String libelle;
    private String typeCompte;
    private String statut;
    private UUID clientId;
    private String codeBanque;
    private String codeGuichet;
    private String cleRib;
    private String iban;
    private String bic;

    private List<MandataireCommand> mandataires;

}
