package com.adouk.finacombackend.domain.aggregates;

import com.adouk.finacombackend.domain.valueObjects.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ReleveCompte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateReception;
    private LocalDateTime dateRejet;


    @ManyToOne
    private CompteBancaire compteBancaire;

    private Date intervalleDebut;
    private Date intervalleFin;

    private String fichierReleve;

    @ManyToOne
    private User deliveredBy;

    private String commentaire;

    private String statut;
}
