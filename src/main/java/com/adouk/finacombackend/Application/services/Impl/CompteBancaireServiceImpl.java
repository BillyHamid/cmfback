package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.CompteBancaireService;
import com.adouk.finacombackend.Application.services.Interfaces.PieceJustificativeService;
import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.commands.CompteBancaireCommand;
import com.adouk.finacombackend.domain.commands.MandataireCommand;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.infrastructure.repositories.*;
import com.adouk.finacombackend.infrastructure.rest.dto.CompteBancaireDto;
import com.adouk.finacombackend.infrastructure.rest.dto.MandataireDto;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteBancaireServiceImpl implements CompteBancaireService {

    private final CompteBancaireRepository compteBancaireRepository;
    private final MandataireRepository mandataireRepository;
    private final ClientRepository clientRepository;
    private final PieceJustificativeService pieceJustificativeService;
    private final DocumentRequisRepository documentRequisRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CompteBancaireDto creerCompte(CompteBancaireCommand command, Map<String, MultipartFile> fichiers) {
        System.out.println("Create CompteBancaire with command " + command);
        Client client = clientRepository.findById(command.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

        CompteBancaire compte = modelMapper.map(command, CompteBancaire.class);
        compte.setClient(client);

        CompteBancaire saved = compteBancaireRepository.save(compte);
        int index = 0;

        for (MandataireCommand mCmd : command.getMandataires()) {
            Mandataire mandataire = modelMapper.map(mCmd, Mandataire.class);
            mandataire.setCompteBancaire(saved);
            Mandataire finalMandataire = mandataireRepository.save(mandataire);

            List<PieceJustificativeDto> pieces = new ArrayList<>();

            String prefix = "mandataires[" + index + "].fichiersJustificatifs[";

            fichiers.forEach((key, file) -> {
                if (key.startsWith(prefix)) {
                    String uuid = key.substring(prefix.length(), key.length() - 1);
                    documentRequisRepository.findById(UUID.fromString(uuid)).ifPresent(documentRequis -> {
                        pieceJustificativeService.createPiece(
                                documentRequis.getLibelle(),
                                documentRequis.getDescription(),
                                file,
                                null,
                                finalMandataire
                        );
                    });
                }
            });

        }
        if (client.getKycStatus() == ClientStatus.ACCOUNT_CREATION_PENDING) {
            client.setKycStatus(ClientStatus.VALIDE);
            clientRepository.save(client);

            Optional<User> user = userRepository.findByUsername(client.getTelephone1());
            if (user.isEmpty()) {
                UserCommand userCommand = new UserCommand();
                userCommand.setUsername(client.getTelephone1());
                userCommand.setLastname(client.getLibelle());
                userCommand.setFirstname(client.getLibelle());
                userCommand.setClient(client);
                userCommand.setEmail(client.getEmail());
                userCommand.setPhone(client.getTelephone1());

                userService.createClient(userCommand);
            }

        }

        return modelMapper.map(saved, CompteBancaireDto.class);
    }

    @Override
    public CompteBancaireDto findById(UUID id) {
        CompteBancaire compte = compteBancaireRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé"));
        return modelMapper.map(compte, CompteBancaireDto.class);
    }

    @Override
    public List<CompteBancaireDto> getClientComptes(UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

        return compteBancaireRepository.findAllByClient(client).stream()
                .map(compte -> {
                    CompteBancaireDto compteDto = modelMapper.map(compte, CompteBancaireDto.class);

                    List<MandataireDto> mandataireDtos = mandataireRepository.findByCompteBancaireId(compte.getId()).stream()
                            .map(mandataire -> {
                                MandataireDto dto = modelMapper.map(mandataire, MandataireDto.class);
                                dto.setPiecesJustificatives(
                                        pieceJustificativeService.getByMandataire(mandataire.getId())
                                );
                                return dto;
                            })
                            .collect(Collectors.toList());

                    compteDto.setMandataires(mandataireDtos);
                    return compteDto;
                })
                .collect(Collectors.toList());


    }

    @Override
    public List<CompteBancaireDto> getCurrentClientComptes() {
        User user = authenticationService.getConectedUser();
        Client client = user.getClient();
        return getClientComptes(client.getId());
    }
}
