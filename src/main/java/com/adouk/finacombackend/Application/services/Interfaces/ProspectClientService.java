package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProspectClientService {

    ProspectClientPhysiqueDto creerClientPhysique(ProspectClientPhysiqueCommand command);
    ProspectClientPhysiqueDto creerClientPhysique(ProspectClientPhysiqueCommand command, User agentCommercial);
    ProspectClientPhysiqueDto findClientPhysiqueById(UUID id);

    ProspectClientMoralDto creerClientMoral(ProspectClientMoralCommand command);
    ProspectClientMoralDto creerClientMoral(ProspectClientMoralCommand command, User agentCommercial);
    ProspectClientMoralDto findClientMoralById(UUID id);

    ProspectClientAssociationDto creerClientAssociation(ProspectClientAssociationCommand command);
    ProspectClientAssociationDto creerClientAssociation(ProspectClientAssociationCommand command, User agentCommercial);
    ProspectClientAssociationDto findClientAssociationById(UUID id);
}
