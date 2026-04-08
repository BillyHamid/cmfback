package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "dossiers_clients")
public class DossierClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private LocalDateTime dateSoumission;
    private LocalDateTime dateDecision;
    @Enumerated(EnumType.STRING)
    private StatusDossierClient statutDossier;
    @Enumerated(EnumType.STRING)
    private ClientType typeClient;
    @ManyToOne
    @JoinColumn(name = "prospect_client_id")
    private ProspectClient prospectClient;

    private Boolean locked = false;

    @ManyToOne
    private User locker;

    private String commentaire;


    @ManyToOne
    @JoinColumn(name = "validateur_id")
    private User validateur;

    /** Agent commercial ayant créé ce prospect (null si créé via formulaire public) */
    @ManyToOne
    @JoinColumn(name = "agent_commercial_id")
    private User agentCommercial;

}
