package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.configs.ClientNumberUtil;
import com.adouk.finacombackend.Application.services.Interfaces.*;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.repositories.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProspectClientServiceImpl implements ProspectClientService {

    private final ProspectClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProspectClientPhysiqueRepository prospectClientPhysiqueRepository;
    private final ClientPhysiqueRepository clientPhysiqueRepository;
    private final AuthenticationService authenticationService;
    private final ProspectPieceJustificativeService prospectPieceJustificativeService;
    private final ProspectClientMoralRepository prospectClientMoralRepository;
    private final DossierClientRepository dossierClientRepository;
    private final DocumentRequisRepository documentRequisRepository;
    private final ProspectClientAssociationRepository prospectClientAssociationRepository;
    private final PieceJustificativeRepository pieceJustificativeRepository;

    @Override
    public ProspectClientPhysiqueDto creerClientPhysique(ProspectClientPhysiqueCommand cmd) {
        return creerClientPhysique(cmd, null);
    }

    @Override
    public ProspectClientPhysiqueDto creerClientPhysique(ProspectClientPhysiqueCommand cmd, User agentCommercial) {
        ProspectClientPhysique client = new ProspectClientPhysique();
        client.setNom(cmd.getNom());
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        client.setPrenom(cmd.getPrenom());
        client.setDateNaissance(cmd.getDateNaissance());
        client.setLieuNaissance(cmd.getLieuNaissance());
        client.setProfession(cmd.getProfession());
        client.setBp(cmd.getBp());
        client.setEmployeur(cmd.getEmployeur());
        client.setAdresseEmployeur(cmd.getAdresseEmployeur());
        client.setNumeroCNIB(cmd.getNumeroCNIB());
        client.setAdresse(cmd.getAdresse());
        client.setEmail(cmd.getEmail());
        client.setTelephone1(cmd.getTelephone1());

        ProspectClient savedClient = prospectClientPhysiqueRepository.save(client);

        List<PieceJustificativeDto> pieces = new ArrayList<>();

        if (cmd.getFichiersJustificatifs() != null) {
            cmd.getFichiersJustificatifs().forEach((uuid, file) -> {
                documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                    pieces.add(
                            prospectPieceJustificativeService.createPiece(
                                    documentRequis.getLibelle(),
                                    documentRequis.getDescription(),
                                    file,
                                    savedClient
                            )
                    );
                });
            });
        }

        // Suite inchangée
        ProspectClientPhysiqueDto dto = new ProspectClientPhysiqueDto();
        BeanUtils.copyProperties(savedClient, dto);
        dto.setPiecesJustificatives(pieces);

        DossierClient dossierClient = new DossierClient();
        dossierClient.setDateSoumission(LocalDateTime.now());
        dossierClient.setProspectClient(savedClient);
        dossierClient.setTypeClient(ClientType.PHYSIQUE);
        dossierClient.setStatutDossier(StatusDossierClient.PENDING);
        dossierClient.setAgentCommercial(agentCommercial);
        dossierClientRepository.save(dossierClient);
        return dto;
    }

    @Override
    public ProspectClientPhysiqueDto findClientPhysiqueById(UUID id) {
        ProspectClientPhysique client = prospectClientPhysiqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client physique non trouvé"));

        ProspectClientPhysiqueDto dto = modelMapper.map(client, ProspectClientPhysiqueDto.class);

        List<PieceJustificativeDto> fichiers = prospectPieceJustificativeService.getByClient(client.getId());
        dto.setPiecesJustificatives(fichiers);

        return dto;
    }

    @Override
    public ProspectClientMoralDto findClientMoralById(UUID id) {
        ProspectClientMoral client = prospectClientMoralRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client moral non trouvé"));

        ProspectClientMoralDto dto = modelMapper.map(client, ProspectClientMoralDto.class);

        List<PieceJustificativeDto> fichiers = prospectPieceJustificativeService.getByClient(client.getId());
        dto.setPiecesJustificatives(fichiers);

        return dto;
    }



    @Override
    public ProspectClientMoralDto creerClientMoral(ProspectClientMoralCommand command) {
        return creerClientMoral(command, null);
    }

    @Override
    public ProspectClientMoralDto creerClientMoral(ProspectClientMoralCommand command, User agentCommercial) {
        ProspectClientMoral client = modelMapper.map(command, ProspectClientMoral.class);
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        ProspectClientMoral saved = prospectClientMoralRepository.save(client);

        List<PieceJustificativeDto> pieces = new ArrayList<>();

        if (command.getFichiersJustificatifs() != null) {
            command.getFichiersJustificatifs().forEach((uuid, file) -> {
                documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                    pieces.add(
                            prospectPieceJustificativeService.createPiece(
                                    documentRequis.getLibelle(),
                                    documentRequis.getDescription(),
                                    file,
                                    saved
                            )
                    );
                });
            });
        }

        ProspectClientMoralDto dto = new ProspectClientMoralDto();
        BeanUtils.copyProperties(saved, dto);
        dto.setPiecesJustificatives(pieces);
        DossierClient dossierClient = new DossierClient();
        dossierClient.setDateSoumission(LocalDateTime.now());
        dossierClient.setProspectClient(saved);
        dossierClient.setTypeClient(ClientType.MORALE);
        dossierClient.setStatutDossier(StatusDossierClient.PENDING);
        dossierClient.setAgentCommercial(agentCommercial);
        dossierClientRepository.save(dossierClient);

        return dto;
    }


    @Override
    public ProspectClientAssociationDto creerClientAssociation(ProspectClientAssociationCommand command) {
        return creerClientAssociation(command, null);
    }

    @Override
    public ProspectClientAssociationDto creerClientAssociation(ProspectClientAssociationCommand command, User agentCommercial) {
        ProspectClientAssociation client = modelMapper.map(command, ProspectClientAssociation.class);
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        ProspectClientAssociation saved = prospectClientAssociationRepository.save(client);

        List<PieceJustificativeDto> pieces = new ArrayList<>();

        if (command.getFichiersJustificatifs() != null) {
            command.getFichiersJustificatifs().forEach((uuid, file) -> {
                documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                    pieces.add(
                            prospectPieceJustificativeService.createPiece(
                                    documentRequis.getLibelle(),
                                    documentRequis.getDescription(),
                                    file,
                                    saved
                            )
                    );
                });
            });
        }

        ProspectClientAssociationDto dto = new ProspectClientAssociationDto();
        BeanUtils.copyProperties(saved, dto);
        dto.setPiecesJustificatives(pieces);
        DossierClient dossierClient = new DossierClient();
        dossierClient.setDateSoumission(LocalDateTime.now());
        dossierClient.setProspectClient(saved);
        dossierClient.setTypeClient(ClientType.ASSOCIATION);
        dossierClient.setStatutDossier(StatusDossierClient.PENDING);
        dossierClient.setAgentCommercial(agentCommercial);
        dossierClientRepository.save(dossierClient);
        return dto;
    }

    @Override
    public ProspectClientAssociationDto findClientAssociationById(UUID id) {
        ProspectClientAssociation client = prospectClientAssociationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client association non trouvé"));
        ProspectClientAssociationDto dto = modelMapper.map(client, ProspectClientAssociationDto.class);
        List<PieceJustificativeDto> fichiers = prospectPieceJustificativeService.getByClient(client.getId());
        dto.setPiecesJustificatives(fichiers);
        return dto;
    }



}
