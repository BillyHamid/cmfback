package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.repositories.ClientCountByAgenceProjection;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    ClientPhysiqueDto creerClientPhysique(ClientPhysiqueCommand command);
    Page<ClientPhysiqueDto> rechercherClientsPhysique(ClientFilterCommand command, Pageable pageable);
    ClientPhysiqueDto updateClientPhysique(UUID id, ClientPhysiqueCommand command);

    ClientMoralDto creerClientMoral(ClientMoralCommand command);
    Page<ClientMoralDto> rechercherClientsMoral(ClientFilterCommand command, Pageable pageable);
    ClientMoralDto updateClientMoral(UUID id,ClientMoralCommand command);

    ClientAssociationDto creerClientAssociation(ClientAssociationCommand command);
    Page<ClientAssociationDto> rechercherClientsAssociation(ClientFilterCommand command, Pageable pageable);
    ClientAssociationDto updateClientAssociation(UUID id, ClientAssociationCommand command);

    Object getCurrentClient();
    GestionnaireDto getCurrentClientGestionnaire();

    ClientDto deleteClient(UUID id);
    ClientDto enableMobileAccount(UUID id);

    List<ClientCountByAgenceProjection> getClientCountByAgence();



}
