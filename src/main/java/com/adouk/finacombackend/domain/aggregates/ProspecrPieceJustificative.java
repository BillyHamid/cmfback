package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "pieces_justificatives_prospects")
public class ProspecrPieceJustificative {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String libelle;
    private String extension;
    private LocalDateTime dateEnregistrement;
    private String urlFichier;

    @ManyToOne
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "prospect_client_id")
    private ProspectClient client;

}

