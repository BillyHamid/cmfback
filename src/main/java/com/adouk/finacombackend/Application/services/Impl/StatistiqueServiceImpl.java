package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.ClientService;
import com.adouk.finacombackend.Application.services.Interfaces.StatistiqueService;
import com.adouk.finacombackend.domain.aggregates.ClientStatus;
import com.adouk.finacombackend.domain.aggregates.StatusDossierClient;
import com.adouk.finacombackend.infrastructure.repositories.*;
import com.adouk.finacombackend.infrastructure.rest.dto.ClientCountByTypeDto;
import com.adouk.finacombackend.infrastructure.rest.dto.CountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatistiqueServiceImpl implements StatistiqueService {

    private final DossierClientRepository dossierClientRepository;
    private final ClientRepository clientRepository;
    private final ClientPhysiqueRepository clientPhysiqueRepository;
    private final ClientMoralRepository clientMoralRepository;
    private final ClientAssociationRepository cClientAssociationRepository;
    private final ClientAssociationRepository clientAssociationRepository;

    @Override
    public CountDto getDossierCount() {
        int total = dossierClientRepository.findAll().size();
        int pending = dossierClientRepository.findByStatutDossier(StatusDossierClient.PENDING).size();
        int validated = dossierClientRepository.findByStatutDossier(StatusDossierClient.VALIDATED).size();
        int rejected = dossierClientRepository.findByStatutDossier(StatusDossierClient.REJECTED).size();
        CountDto countDto = new CountDto();
        countDto.setPending(pending);
        countDto.setRejected(rejected);
        countDto.setTotal(total);
        countDto.setValidated(validated);
        return countDto;
    }

    @Override
    public CountDto getClientCount() {
        int total = clientRepository.findAll().size();
        int pending = clientRepository.findByKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING).size();
        int validated = clientRepository.findByKycStatus(ClientStatus.VALIDE).size();
        CountDto countDto = new CountDto();
        countDto.setPending(pending);
        countDto.setTotal(total);
        countDto.setValidated(validated);
        return countDto;
    }

    @Override
    public ClientCountByTypeDto getCLientCountByType() {
        ClientCountByTypeDto clientCountByTypeDto = new ClientCountByTypeDto();
        clientCountByTypeDto.setEntreprise(clientMoralRepository.findAll().size());
        clientCountByTypeDto.setPhysic(clientPhysiqueRepository.findAll().size());
        clientCountByTypeDto.setAssociation(clientAssociationRepository.findAll().size());
        return clientCountByTypeDto;
    }
}
