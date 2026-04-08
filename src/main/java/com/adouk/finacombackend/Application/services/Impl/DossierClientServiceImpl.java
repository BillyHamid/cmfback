package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.DossierClientService;
import com.adouk.finacombackend.Application.services.Interfaces.ProspectPieceJustificativeService;
import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.beans.PasswordUtils;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.domain.commands.ValidationDossierCommand;
import com.adouk.finacombackend.infrastructure.repositories.*;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DossierClientServiceImpl implements DossierClientService {

     private final DossierClientRepository dossierClientRepository;
     private final AuthenticationService authenticationService;
        private final ProspectClientPhysiqueRepository prospectClientPhysiqueRepository;
        private final ProspectClientMoralRepository prospectClientMoralRepository;
        private final ProspectClientAssociationRepository prospectClientAssociationRepository;
        private final ClientPhysiqueRepository clientPhysiqueRepository;
        private final ClientMoralRepository clientMoralRepository;
        private final ClientAssociationRepository clientAssociationRepository;
        private final ModelMapper modelMapper;
        private final ProspectPieceJustificativeService prospectPieceJustificativeService;
        private final PieceJustificativeRepository pieceJustificativeRepository;
        private final UserRepository userRepository;
        private final UserService userService;
        private final AgenceRepository agenceRepository;
    private final PasswordEncoder encoder;


    @Override
    public List<DossierClient> getPending() {
        return dossierClientRepository.findByStatutDossier(StatusDossierClient.PENDING);
    }

    @Override
    public List<DossierClient> getTraiter() {
        return dossierClientRepository.findByStatutDossierDiff(StatusDossierClient.PENDING);
    }

    @Override
    public DossierClient validateKyc(ValidationDossierCommand command) {
        DossierClient dossierClient = dossierClientRepository.findById(command.getDossierUuid()).orElseThrow();
        dossierClient.setStatutDossier(StatusDossierClient.VALIDATED);
        dossierClient.setDateDecision(LocalDateTime.now());
        dossierClient.setValidateur(authenticationService.getConectedUser());
        dossierClientRepository.save(dossierClient);
        String username = "";
        String name = "";
        String lastname = "";
        String firstname = "";
        Client client = null;
        if (dossierClient.getTypeClient().equals(ClientType.PHYSIQUE)) {
            ProspectClientPhysique prospectClientPhysique = prospectClientPhysiqueRepository.findById(dossierClient.getProspectClient().getId()).orElseThrow();
            ClientPhysique clientPhysique = modelMapper.map(prospectClientPhysique, ClientPhysique.class);
            username = prospectClientPhysique.getTelephone1();
            lastname = prospectClientPhysique.getNom();
            firstname = prospectClientPhysique.getPrenom();
            clientPhysique.setKycValidated(true);
            clientPhysique.setKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING);
            clientPhysique.setDeleted(false);
            clientPhysique.setIdSib(command.getIdSib());
            clientPhysique.setValidateur(authenticationService.getConectedUser());
            clientPhysique.setAgenceRattachee(agenceRepository.findById(command.getAgentUuid()).orElseThrow());
            clientPhysique = clientPhysiqueRepository.save(clientPhysique);
            client = clientPhysique;
            for (PieceJustificativeDto piece : prospectPieceJustificativeService.getByClient(prospectClientPhysique.getId())) {
                PieceJustificative pieceJustificative = modelMapper.map(piece, PieceJustificative.class);
                pieceJustificative.setClient(clientPhysique);
                pieceJustificativeRepository.save(pieceJustificative);
            }
        }
        if (dossierClient.getTypeClient().equals(ClientType.MORALE)) {
            ProspectClientMoral prospectClientMoral = prospectClientMoralRepository.findById(dossierClient.getProspectClient().getId()).orElseThrow();
            ClientMoral clientMoral = modelMapper.map(prospectClientMoral, ClientMoral.class);
            username = prospectClientMoral.getTelResponsable();
            lastname = prospectClientMoral.getNomResponsable();
            firstname = prospectClientMoral.getPrenomResponsable();
            clientMoral.setKycValidated(true);
            clientMoral.setKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING);
            clientMoral.setDeleted(false);
            clientMoral.setIdSib(command.getIdSib());
            clientMoral.setValidateur(authenticationService.getConectedUser());
            clientMoral.setAgenceRattachee(agenceRepository.findById(command.getAgentUuid()).orElseThrow());
            clientMoral = clientMoralRepository.save(clientMoral);
            client = clientMoral;
            for (PieceJustificativeDto piece : prospectPieceJustificativeService.getByClient(prospectClientMoral.getId())) {
                PieceJustificative pieceJustificative = modelMapper.map(piece, PieceJustificative.class);
                pieceJustificative.setClient(clientMoral);
                pieceJustificativeRepository.save(pieceJustificative);
            }
        }
        if(dossierClient.getTypeClient().equals(ClientType.ASSOCIATION)) {
            ProspectClientAssociation prospectClientAssociation = prospectClientAssociationRepository.findById(dossierClient.getProspectClient().getId()).orElseThrow();
            ClientAssociation clientAssociation = modelMapper.map(prospectClientAssociation, ClientAssociation.class);
            username = prospectClientAssociation.getTelRepresentant();
            lastname = prospectClientAssociation.getNomRepresentant();
            firstname = prospectClientAssociation.getPrenomRepresentant();
            clientAssociation.setKycValidated(true);
            clientAssociation.setDeleted(false);
            clientAssociation.setIdSib(command.getIdSib());
            clientAssociation.setKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING);
            clientAssociation.setValidateur(authenticationService.getConectedUser());
            clientAssociation.setAgenceRattachee(agenceRepository.findById(command.getAgentUuid()).orElseThrow());
            clientAssociation = clientAssociationRepository.save(clientAssociation);
            client = clientAssociation;
            for (PieceJustificativeDto piece : prospectPieceJustificativeService.getByClient(prospectClientAssociation.getId())) {
                PieceJustificative pieceJustificative = modelMapper.map(piece, PieceJustificative.class);
                pieceJustificative.setClient(clientAssociation);
                pieceJustificativeRepository.save(pieceJustificative);
            }
        }

//        UserCommand userCommand = new UserCommand();
//        userCommand.setUsername(username);
//        userCommand.setLastname(lastname);
//        userCommand.setFirstname(firstname);
//        userCommand.setClient(client);
//        userCommand.setEmail(client.getEmail());
//        userCommand.setPhone(client.getTelephone1());
//
//        userService.createClient(userCommand);
        return dossierClient;
    }

    @Override
    public DossierClient getById(UUID id) {
        return dossierClientRepository.findById(id).orElseThrow();
    }

    @Override
    public DossierClient rejectedKyc(ValidationDossierCommand command) {
        log.info("Rejet du dossier client : {}", command.getDossierUuid());
        log.info("Commentaire : {}", command.getCommentaire());
        DossierClient dossierClient = dossierClientRepository.findById(command.getDossierUuid()).orElseThrow();
        dossierClient.setStatutDossier(StatusDossierClient.REJECTED);
        dossierClient.setDateDecision(LocalDateTime.now());
        dossierClient.setCommentaire(command.getCommentaire());
        dossierClient = dossierClientRepository.save(dossierClient);
        String username = "";
        String name = "";
        String lastname = "";
        String firstname = "";
        Client client = null;
        if (dossierClient.getTypeClient().equals(ClientType.PHYSIQUE)) {
            ProspectClientPhysique prospectClientPhysique = prospectClientPhysiqueRepository.findById(dossierClient.getProspectClient().getId()).orElseThrow();
            ClientPhysique clientPhysique = modelMapper.map(prospectClientPhysique, ClientPhysique.class);
            username = prospectClientPhysique.getTelephone1();
            lastname = prospectClientPhysique.getNom();
            firstname = prospectClientPhysique.getPrenom();
            clientPhysique.setKycValidated(false);
            clientPhysique.setKycStatus(ClientStatus.REJECTED);
            clientPhysique.setDeleted(false);
            clientPhysique.setIdSib(command.getIdSib());
            clientPhysique.setValidateur(authenticationService.getConectedUser());
            clientPhysique = clientPhysiqueRepository.save(clientPhysique);
            client = clientPhysique;
            for (PieceJustificativeDto piece : prospectPieceJustificativeService.getByClient(prospectClientPhysique.getId())) {
                PieceJustificative pieceJustificative = modelMapper.map(piece, PieceJustificative.class);
                pieceJustificative.setClient(clientPhysique);
                pieceJustificativeRepository.save(pieceJustificative);
            }
        }
        if (dossierClient.getTypeClient().equals(ClientType.MORALE)) {
            ProspectClientMoral prospectClientMoral = prospectClientMoralRepository.findById(dossierClient.getProspectClient().getId()).orElseThrow();
            ClientMoral clientMoral = modelMapper.map(prospectClientMoral, ClientMoral.class);
            username = prospectClientMoral.getTelResponsable();
            lastname = prospectClientMoral.getNomResponsable();
            firstname = prospectClientMoral.getPrenomResponsable();
            clientMoral.setKycValidated(true);
            clientMoral.setKycStatus(ClientStatus.REJECTED);
            clientMoral.setDeleted(false);
            clientMoral.setIdSib(command.getIdSib());
            clientMoral.setValidateur(authenticationService.getConectedUser());
            clientMoral = clientMoralRepository.save(clientMoral);
            client = clientMoral;
            for (PieceJustificativeDto piece : prospectPieceJustificativeService.getByClient(prospectClientMoral.getId())) {
                PieceJustificative pieceJustificative = modelMapper.map(piece, PieceJustificative.class);
                pieceJustificative.setClient(clientMoral);
                pieceJustificativeRepository.save(pieceJustificative);
            }
        }
        if(dossierClient.getTypeClient().equals(ClientType.ASSOCIATION)) {
            ProspectClientAssociation prospectClientAssociation = prospectClientAssociationRepository.findById(dossierClient.getProspectClient().getId()).orElseThrow();
            ClientAssociation clientAssociation = modelMapper.map(prospectClientAssociation, ClientAssociation.class);
            username = prospectClientAssociation.getTelRepresentant();
            lastname = prospectClientAssociation.getNomRepresentant();
            firstname = prospectClientAssociation.getPrenomRepresentant();
            clientAssociation.setKycValidated(true);
            clientAssociation.setDeleted(false);
            clientAssociation.setIdSib(command.getIdSib());
            clientAssociation.setKycStatus(ClientStatus.REJECTED);
            clientAssociation.setValidateur(authenticationService.getConectedUser());
            clientAssociation = clientAssociationRepository.save(clientAssociation);
            client = clientAssociation;
            for (PieceJustificativeDto piece : prospectPieceJustificativeService.getByClient(prospectClientAssociation.getId())) {
                PieceJustificative pieceJustificative = modelMapper.map(piece, PieceJustificative.class);
                pieceJustificative.setClient(clientAssociation);
                pieceJustificativeRepository.save(pieceJustificative);
            }
        }
        return dossierClient;
    }
}
