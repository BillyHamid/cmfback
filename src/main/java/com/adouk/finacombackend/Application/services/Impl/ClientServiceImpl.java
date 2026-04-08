package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.configs.ClientNumberUtil;
import com.adouk.finacombackend.Application.services.Interfaces.ClientService;
import com.adouk.finacombackend.Application.services.Interfaces.PieceJustificativeService;
import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.domain.valueObjects.TypePiece;
import com.adouk.finacombackend.infrastructure.repositories.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ClientPhysiqueRepository clientPhysiqueRepository;
    private final AuthenticationService authenticationService;
    private final PieceJustificativeService pieceJustificativeService;
    private final ClientMoralRepository clientMoralRepository;
    private final DossierClientRepository dossierClientRepository;
    private final DocumentRequisRepository documentRequisRepository;
    private final ClientAssociationRepository clientAssociationRepository;
    private final AgenceRepository agenceRepository;

    @Override
    @Transactional
    public ClientPhysiqueDto creerClientPhysique(ClientPhysiqueCommand cmd) {
        ClientPhysique client = new ClientPhysique();
        client.setNom(cmd.getNom());
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        client.setPrenom(cmd.getPrenom());
        client.setIdSib(cmd.getIdSib());
        client.setDateNaissance(cmd.getDateNaissance());
        client.setDeleted(false);
        client.setLieuNaissance(cmd.getLieuNaissance());
        client.setProfession(cmd.getProfession());
        client.setBp(cmd.getBp());
        if (cmd.getAgenceId() != null) {
            client.setAgenceRattachee(agenceRepository.findById(cmd.getAgenceId()).orElseThrow());
        }
        client.setEmployeur(cmd.getEmployeur());
        client.setAdresseEmployeur(cmd.getAdresseEmployeur());
        client.setNumeroCNIB(cmd.getNumeroCNIB());
        client.setAdresse(cmd.getAdresse());
        client.setEmail(cmd.getEmail());
        client.setTelephone1(cmd.getTelephone1());
        client.setTelephone2(cmd.getTelephone2());
        client.setKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING);
        client.setKycValidated(true);
        client.setValidateur(authenticationService.getConectedUser());

        ClientPhysique savedClient = clientPhysiqueRepository.save(client);

        List<PieceJustificativeDto> pieces = new ArrayList<>();

        cmd.getFichiersJustificatifs().forEach((uuid, file) -> {
            documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                pieces.add(
                        pieceJustificativeService.createPiece(
                                documentRequis.getLibelle(),
                                documentRequis.getDescription(),
                                file,
                                savedClient,
                                null
                        )
                );
            });
        });

        // Suite inchangée
        ClientPhysiqueDto dto = new ClientPhysiqueDto();
        BeanUtils.copyProperties(savedClient, dto);
        dto.setPiecesJustificatives(pieces);

        if (cmd.isEnableMobileAccess()) {
            UserCommand userCommand = new UserCommand();
            userCommand.setUsername(cmd.getTelephone1());
            userCommand.setLastname(cmd.getNom());
            userCommand.setFirstname(cmd.getPrenom());
            userCommand.setClient(client);
            userCommand.setEmail(cmd.getEmail());
            userCommand.setPhone(cmd.getTelephone1());

            userService.createClient(userCommand);
        }

        return dto;
    }


    @Override
    public ClientMoralDto creerClientMoral(ClientMoralCommand command) {
        ClientMoral client = modelMapper.map(command, ClientMoral.class);
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        client.setKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING);
        client.setKycValidated(true);
        client.setDeleted(false);
        client.setValidateur(authenticationService.getConectedUser());
        if (command.getAgenceId() != null) {
            client.setAgenceRattachee(agenceRepository.findById(command.getAgenceId()).orElseThrow());
        }
        ClientMoral saved = clientMoralRepository.save(client);

        List<PieceJustificativeDto> pieces = new ArrayList<>();

        if (command.getFichiersJustificatifs() != null) {
            command.getFichiersJustificatifs().forEach((uuid, file) -> {
                documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                    pieces.add(
                            pieceJustificativeService.createPiece(
                                    documentRequis.getLibelle(),
                                    documentRequis.getDescription(),
                                    file,
                                    saved,
                                    null
                            )
                    );
                });
            });
        }

        ClientMoralDto dto = new ClientMoralDto();
        BeanUtils.copyProperties(saved, dto);
        dto.setPiecesJustificatives(pieces); // si supporté dans le DTO

        if (command.isEnableMobileAccess()) {
            UserCommand userCommand = new UserCommand();
            userCommand.setUsername(command.getTelephone1());
            userCommand.setLastname(command.getRaisonSociale());
            userCommand.setFirstname(command.getFormeJuridique());
            userCommand.setClient(client);
            userCommand.setEmail(command.getEmail());
            userCommand.setPhone(command.getTelephone1());

            userService.createClient(userCommand);
        }

        return dto;
    }



    @Override
    public Page<ClientPhysiqueDto> rechercherClientsPhysique(ClientFilterCommand command, Pageable pageable) {
        Specification<ClientPhysique> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), false));

            if (command.getSearch() != null && !command.getSearch().isBlank()) {
                String likePattern = command.getSearch().toLowerCase() + "%";
                Predicate nom = cb.like(cb.lower(root.get("nom")), likePattern);
                Predicate prenom = cb.like(cb.lower(root.get("prenom")), likePattern);
                Predicate email = cb.like(cb.lower(root.get("email")), likePattern);
                predicates.add(cb.or(nom, prenom, email));
            }

            if (command.getStatus() != null) {
                predicates.add(cb.equal(root.get("kycStatus"), command.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return clientPhysiqueRepository.findAll(spec, pageable)
                .map(client -> {
                    ClientPhysiqueDto dto = new ClientPhysiqueDto();
                    BeanUtils.copyProperties(client, dto);
                    if (client.getValidateur() != null) {
                        log.info("User : {}", client.getId());
                        dto.setHasMobileAccess(userRepository.findByClientId(client.getId()).isPresent());
                    }
                    dto.setAgentValidateur(client.getValidateur().getAgent());

                    return dto;
                });
    }


    @Override
    public Page<ClientMoralDto> rechercherClientsMoral(ClientFilterCommand command, Pageable pageable) {
        Specification<ClientMoral> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), false));

            if (command.getSearch() != null && !command.getSearch().isBlank()) {
                String likePattern = command.getSearch().toLowerCase() + "%";
                Predicate raisonSociale = cb.like(cb.lower(root.get("raisonSociale")), likePattern);
                predicates.add(cb.or(raisonSociale));
            }

            if (command.getStatus() != null) {
                predicates.add(cb.equal(root.get("kycStatus"), command.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return clientMoralRepository.findAll(spec, pageable)
                .map(client -> {
                    ClientMoralDto dto = new ClientMoralDto();
                    BeanUtils.copyProperties(client, dto);
                    if (client.getValidateur() != null) {
                        dto.setAgentValidateur(client.getValidateur().getAgent());
                    }
                    dto.setHasMobileAccess(userRepository.findByClientId(client.getId()).isPresent());
                    return dto;
                });
    }


    @Override
    public ClientAssociationDto creerClientAssociation(ClientAssociationCommand command) {
        ClientAssociation client = modelMapper.map(command, ClientAssociation.class);
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        client.setKycStatus(ClientStatus.ACCOUNT_CREATION_PENDING);
        client.setKycValidated(true);
        client.setDeleted(false);
        client.setValidateur(authenticationService.getConectedUser());
        if (command.getAgenceId() != null) {
            client.setAgenceRattachee(agenceRepository.findById(command.getAgenceId()).orElseThrow());
        }
        ClientAssociation saved = clientAssociationRepository.save(client);

        List<PieceJustificativeDto> pieces = new ArrayList<>();

        if (command.getFichiersJustificatifs() != null) {
            command.getFichiersJustificatifs().forEach((uuid, file) -> {
                documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                    pieces.add(
                            pieceJustificativeService.createPiece(
                                    documentRequis.getLibelle(),
                                    documentRequis.getDescription(),
                                    file,
                                    saved,
                                    null
                            )
                    );
                });
            });
        }

        ClientAssociationDto dto = new ClientAssociationDto();
        BeanUtils.copyProperties(saved, dto);
        dto.setPiecesJustificatives(pieces);

        if (command.isEnableMobileAccess()) {
            UserCommand userCommand = new UserCommand();
            userCommand.setUsername(command.getTelephone1());
            userCommand.setLastname(command.getNomOrganisation());
            userCommand.setFirstname(command.getTypeOrganisation());
            userCommand.setClient(client);
            userCommand.setEmail(command.getEmail());
            userCommand.setPhone(command.getTelephone1());

            userService.createClient(userCommand);
        }
        return dto;
    }

    @Override
    public Page<ClientAssociationDto> rechercherClientsAssociation(ClientFilterCommand command, Pageable pageable) {
        Specification<ClientAssociation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), false));

            if (command.getSearch() != null && !command.getSearch().isBlank()) {
                String likePattern = command.getSearch().toLowerCase() + "%";
                Predicate nomOrganisation = cb.like(cb.lower(root.get("nomOrganisation")), likePattern);
                Predicate typeOrganisation = cb.like(cb.lower(root.get("typeOrganisation")), likePattern);
                Predicate email = cb.like(cb.lower(root.get("email")), likePattern);
                predicates.add(cb.or(nomOrganisation, typeOrganisation, email));
            }

            if (command.getStatus() != null) {
                predicates.add(cb.equal(root.get("kycStatus"), command.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return clientAssociationRepository.findAll(spec, pageable)
                .map(client -> {
                    ClientAssociationDto dto = new ClientAssociationDto();
                    BeanUtils.copyProperties(client, dto);
                    if (client.getValidateur() != null) {
                        dto.setAgentValidateur(client.getValidateur().getAgent());
                    }
                    dto.setHasMobileAccess(userRepository.findByClientId(client.getId()).isPresent());
                    return dto;
                });
    }


    @Override
    public ClientDto deleteClient(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setDeleted(true);
        client.setDeletedAt(LocalDateTime.now());
        client.setDeletedBy(authenticationService.getConectedUser().getAgent().getFirstname() + " " + authenticationService.getConectedUser().getAgent().getLastname());
        client = clientRepository.save(client);
        return modelMapper.map(client, ClientDto.class);
    }

    @Override
    public Object getCurrentClient() {
        User user = authenticationService.getConectedUser();
        Client client = user.getClient();

        if (client instanceof ClientPhysique) {
            return modelMapper.map(clientPhysiqueRepository.findById(client.getId()).orElseThrow(), ClientPhysiquemobileDto.class);
        } else if (client instanceof ClientMoral) {
            return modelMapper.map(clientMoralRepository.findById(client.getId()).orElseThrow(), ClientMoralDto.class);
        } else if (client instanceof ClientAssociation) {
            return modelMapper.map(clientAssociationRepository.findById(client.getId()).orElseThrow(), ClientAssociationDto.class);
        } else {
            throw new IllegalStateException("Type de client inconnu");
        }
    }


    @Override
    public GestionnaireDto getCurrentClientGestionnaire() {
        User user = authenticationService.getConectedUser();
        Client client = user.getClient();
        GestionnaireDto dto = new GestionnaireDto();
        if (client.getAgenceRattachee() == null) {
            return null;
        }
        dto.setFirstname(client.getAgenceRattachee().getGestionnaire().getFirstname());
        dto.setLastname(client.getAgenceRattachee().getGestionnaire().getLastname());
        dto.setPhone(client.getAgenceRattachee().getGestionnaire().getPhone());
        dto.setEmail(client.getAgenceRattachee().getGestionnaire().getEmail());
        dto.setAgencyName(client.getAgenceRattachee().getAgencyName());
        dto.setAgencyAdress(client.getAgenceRattachee().getAgencyAdress());
        dto.setAgencyPhone1(client.getAgenceRattachee().getAgencyPhone1());
        dto.setAgencyPhone2(client.getAgenceRattachee().getAgencyPhone2());
        dto.setAgencyLongitude(client.getAgenceRattachee().getAgencyLongitude());
        dto.setAgencyLatitude(client.getAgenceRattachee().getAgencyLatitude());
        return dto;
    }

    @Override
    public List<ClientCountByAgenceProjection> getClientCountByAgence() {
        return clientRepository.countClientsByAgence();
    }

    @Override
    @Transactional
    public ClientPhysiqueDto updateClientPhysique(UUID id, ClientPhysiqueCommand cmd) {
        ClientPhysique old = clientPhysiqueRepository.findById(id).orElseThrow();
        ClientPhysique client = new ClientPhysique();
        client.setId(id);
        client.setNom(cmd.getNom());
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        client.setPrenom(cmd.getPrenom());
        client.setDateNaissance(cmd.getDateNaissance());
        client.setLieuNaissance(cmd.getLieuNaissance());
        client.setProfession(cmd.getProfession());
        client.setBp(cmd.getBp());
        client.setIdSib(cmd.getIdSib());
        if (cmd.getAgenceId() != null) {
            client.setAgenceRattachee(agenceRepository.findById(cmd.getAgenceId()).orElseThrow());
        }
        client.setEmployeur(cmd.getEmployeur());
        client.setAdresseEmployeur(cmd.getAdresseEmployeur());
        client.setNumeroCNIB(cmd.getNumeroCNIB());
        client.setAdresse(cmd.getAdresse());
        client.setEmail(cmd.getEmail());
        client.setTelephone1(cmd.getTelephone1());
        client.setTelephone2(cmd.getTelephone2());
        client.setKycStatus(old.getKycStatus());
        client.setKycValidated(old.getKycValidated());
        client.setDeleted(old.getDeleted());
        client.setUpdatedBy(authenticationService.getConectedUser().getAgent().getFirstname() + " " + authenticationService.getConectedUser().getAgent().getLastname());
        client.setUpdatedAt(new Date());
        client.setValidateur(old.getValidateur());
        client.setCreatedAt(old.getCreatedAt());
        client.setCreatedBy(old.getCreatedBy());


        ClientPhysique savedClient = clientPhysiqueRepository.save(client);

        ClientPhysiqueDto dto = new ClientPhysiqueDto();
        BeanUtils.copyProperties(savedClient, dto);

        return dto;
    }

    @Override
    @Transactional
    public ClientMoralDto updateClientMoral(UUID id, ClientMoralCommand command) {
        ClientMoral old = clientMoralRepository.findById(id).orElseThrow();

        ClientMoral client = modelMapper.map(command, ClientMoral.class);
        client.setNumeroClient(old.getNumeroClient());
        client.setKycStatus(old.getKycStatus());
        client.setKycValidated(true);
        client.setDeleted(old.getDeleted());
        client.setValidateur(old.getValidateur());
        client.setCreatedAt(old.getCreatedAt());
        client.setCreatedBy(old.getCreatedBy());
        client.setKycValidated(old.getKycValidated());


        client.setId(id);
        client.setUpdatedBy(authenticationService.getConectedUser().getAgent().getFirstname() + " " + authenticationService.getConectedUser().getAgent().getLastname());
        client.setUpdatedAt(new Date());
        if (command.getAgenceId() != null) {
            client.setAgenceRattachee(agenceRepository.findById(command.getAgenceId()).orElseThrow());
        }
        ClientMoral saved = clientMoralRepository.save(client);

        ClientMoralDto dto = new ClientMoralDto();
        BeanUtils.copyProperties(saved, dto);

        return dto;
    }

    @Override
    @Transactional
    public ClientAssociationDto updateClientAssociation(UUID id, ClientAssociationCommand command) {
        ClientAssociation old = clientAssociationRepository.findById(id).orElseThrow();

        ClientAssociation client = modelMapper.map(command, ClientAssociation.class);
        client.setNumeroClient(ClientNumberUtil.generateClientNumber());
        client.setKycStatus(old.getKycStatus());
        client.setKycValidated(old.getKycValidated());
        client.setDeleted(old.getDeleted());
        client.setValidateur(old.getValidateur());
        client.setCreatedAt(old.getCreatedAt());
        client.setCreatedBy(old.getCreatedBy());
        client.setKycValidated(old.getKycValidated());
        client.setId(id);
        client.setUpdatedBy(authenticationService.getConectedUser().getAgent().getFirstname() + " " + authenticationService.getConectedUser().getAgent().getLastname());
        client.setUpdatedAt(new Date());
        if (command.getAgenceId() != null) {
            client.setAgenceRattachee(agenceRepository.findById(command.getAgenceId()).orElseThrow());
        }
        ClientAssociation saved = clientAssociationRepository.save(client);

        ClientAssociationDto dto = new ClientAssociationDto();
        BeanUtils.copyProperties(saved, dto);

        return dto;
    }


    @Override
    public ClientDto enableMobileAccount(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow();

        UserCommand userCommand = new UserCommand();
        userCommand.setClient(client);
        userCommand.setUsername(client.getTelephone1());
        userCommand.setPhone(client.getTelephone1());

        if (client instanceof ClientPhysique physique) {
            userCommand.setFirstname(physique.getPrenom());
            userCommand.setLastname(physique.getNom());
            userCommand.setEmail(physique.getEmail());

        } else if (client instanceof ClientMoral moral) {
            userCommand.setFirstname(moral.getRaisonSociale());
            userCommand.setLastname(moral.getRaisonSociale());
            userCommand.setEmail(moral.getEmail());

        } else if (client instanceof ClientAssociation association) {
            userCommand.setFirstname(association.getNomOrganisation());
            userCommand.setLastname(association.getNomOrganisation());
            userCommand.setEmail(association.getEmail());
        } else {
            throw new IllegalStateException("Type de client inconnu");
        }

        userService.createClient(userCommand);
        return modelMapper.map(client, ClientDto.class);
    }



}
