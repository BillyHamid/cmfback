package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.CommercialService;
import com.adouk.finacombackend.Application.services.Interfaces.ProspectClientService;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.repositories.DossierClientRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import com.adouk.finacombackend.infrastructure.rest.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommercialServiceImpl implements CommercialService {

    private final DossierClientRepository dossierClientRepository;
    private final ProspectClientService prospectClientService;
    private final AuthenticationService authenticationService;

    @Override
    public List<DossierClient> getMyProspects() {
        User currentUser = authenticationService.getConectedUser();
        return dossierClientRepository.findByAgentCommercialOrderByDateSoumissionDesc(currentUser);
    }

    @Override
    public Map<String, Long> getDashboardStats() {
        User currentUser = authenticationService.getConectedUser();
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) dossierClientRepository.findByAgentCommercialOrderByDateSoumissionDesc(currentUser).size());
        stats.put("pending", dossierClientRepository.countByAgentCommercialAndStatutDossier(currentUser, StatusDossierClient.PENDING));
        stats.put("validated", dossierClientRepository.countByAgentCommercialAndStatutDossier(currentUser, StatusDossierClient.VALIDATED));
        stats.put("rejected", dossierClientRepository.countByAgentCommercialAndStatutDossier(currentUser, StatusDossierClient.REJECTED));
        return stats;
    }

    @Override
    public ProspectClientPhysiqueDto creerProspectPhysique(ProspectClientPhysiqueCommand command) {
        User agent = authenticationService.getConectedUser();
        return prospectClientService.creerClientPhysique(command, agent);
    }

    @Override
    public ProspectClientMoralDto creerProspectMoral(ProspectClientMoralCommand command) {
        User agent = authenticationService.getConectedUser();
        return prospectClientService.creerClientMoral(command, agent);
    }

    @Override
    public ProspectClientAssociationDto creerProspectAssociation(ProspectClientAssociationCommand command) {
        User agent = authenticationService.getConectedUser();
        return prospectClientService.creerClientAssociation(command, agent);
    }

    @Override
    public Object getProspectDetail(UUID dossierId) {
        User currentUser = authenticationService.getConectedUser();
        DossierClient dossier = dossierClientRepository.findById(dossierId)
                .orElseThrow(() -> new BusinessException("Dossier non trouvé"));
        if (dossier.getAgentCommercial() == null || !dossier.getAgentCommercial().getUuid().equals(currentUser.getUuid())) {
            throw new BusinessException("Accès non autorisé à ce prospect");
        }
        if (dossier.getTypeClient() == ClientType.PHYSIQUE) {
            return prospectClientService.findClientPhysiqueById(dossier.getProspectClient().getId());
        }
        if (dossier.getTypeClient() == ClientType.MORALE) {
            return prospectClientService.findClientMoralById(dossier.getProspectClient().getId());
        }
        if (dossier.getTypeClient() == ClientType.ASSOCIATION) {
            return prospectClientService.findClientAssociationById(dossier.getProspectClient().getId());
        }
        throw new BusinessException("Type de client inconnu");
    }
}
