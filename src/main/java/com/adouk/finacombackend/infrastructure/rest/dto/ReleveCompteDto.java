package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.CompteBancaire;
import com.adouk.finacombackend.domain.aggregates.User;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class ReleveCompteDto {

    private UUID uuid;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateReception;

    private Date intervalleDebut;
    private Date intervalleFin;
    private String statut;
    private String commentaire;
    private String fichierReleve;
}
