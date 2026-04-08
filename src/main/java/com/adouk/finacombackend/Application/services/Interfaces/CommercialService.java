package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.DossierClient;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service pour les agents commerciaux : prospection, suivi des prospects.
 */
public interface CommercialService {

    /** Liste des prospects créés par l'agent connecté */
    List<DossierClient> getMyProspects();

    /** Statistiques du tableau de bord commercial */
    Map<String, Long> getDashboardStats();

    /** Créer un prospect physique (en tant qu'agent commercial) */
    ProspectClientPhysiqueDto creerProspectPhysique(ProspectClientPhysiqueCommand command);

    /** Créer un prospect moral */
    ProspectClientMoralDto creerProspectMoral(ProspectClientMoralCommand command);

    /** Créer un prospect association */
    ProspectClientAssociationDto creerProspectAssociation(ProspectClientAssociationCommand command);

    /** Détail d'un prospect (vérifie que l'agent en est propriétaire) */
    Object getProspectDetail(UUID dossierId);
}
