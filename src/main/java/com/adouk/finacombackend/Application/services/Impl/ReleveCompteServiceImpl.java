package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.FileStorageService;
import com.adouk.finacombackend.Application.services.Interfaces.NotificationSenderService;
import com.adouk.finacombackend.Application.services.Interfaces.ReleveCompteService;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.commands.ReleveCommand;
import com.adouk.finacombackend.infrastructure.repositories.CompteBancaireRepository;
import com.adouk.finacombackend.infrastructure.repositories.FcmTokenRepository;
import com.adouk.finacombackend.infrastructure.repositories.ReleveCompteRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import com.adouk.finacombackend.infrastructure.rest.dto.ReleveCompteDto;
import com.adouk.finacombackend.infrastructure.rest.dto.ReleveCompteSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReleveCompteServiceImpl implements ReleveCompteService {

    private final ReleveCompteRepository releveCompteRepository;
    private final CompteBancaireRepository compteBancaireRepository;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;
    private final AuthenticationService authenticationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final EmailService emailService;


    @Override
    public List<ReleveCompte> findAll() {
        return releveCompteRepository.findAll();
    }

    @Override
    public ReleveCompte create(ReleveCommand command) {
        CompteBancaire compteBancaire = compteBancaireRepository.findById(command.getCompteBancaireUuid()).orElseThrow();
        ReleveCompte releveCompte = modelMapper.map(command, ReleveCompte.class);
        releveCompte.setCompteBancaire(compteBancaire);
        releveCompte.setDateSoumission(LocalDateTime.now());
        releveCompte.setStatut("EN_ATTENTE");
        releveCompte = releveCompteRepository.save(releveCompte);

        try {
            emailService.sendReleveRequestNotif(releveCompte.getCompteBancaire().getClient().getAgenceRattachee().getGestionnaire().getFirstname(), releveCompte.getCompteBancaire().getClient().getLibelle(), releveCompte.getCompteBancaire().getNumeroCompte(), releveCompte.getCompteBancaire().getClient().getAgenceRattachee().getGestionnaire().getEmail());
        } catch (Exception e) {
            log.error("Error sending email to gestionnaire {}", e.getMessage());
        }
        return releveCompte;
    }

    @Override
    public ReleveCompte submitReleve(UUID releveId, MultipartFile file) {
        ReleveCompte releveCompte = releveCompteRepository.findById(releveId).orElseThrow();
        CompteBancaire compteBancaire = releveCompte.getCompteBancaire();
        String url = fileStorageService.storeToS3(compteBancaire.getClient().getNumeroClient().concat("/").concat("RELEVES"), "RELEVE", file);
        releveCompte.setFichierReleve(url);
        releveCompte.setDeliveredBy(authenticationService.getConectedUser());
        releveCompte.setDateReception(LocalDateTime.now());
        releveCompte.setStatut("VALIDE");
        releveCompte = releveCompteRepository.save(releveCompte);

        // Send notification to client
        List<FcmToken> tokens = fcmTokenRepository.findByClient(compteBancaire.getClient());
        for (FcmToken token : tokens) {
            try{
                firebaseMessagingService.sendNotification(
                        token.getToken(),
                        "Nouveau relevé de compte",
                        "Un nouveau relevé de compte a été ajouté à votre espace."
                );
            } catch (Exception e) {
                System.out.println("Erreur lors de l'envoi de notification");
            }
        }

        return releveCompte;
    }

    @Override
    public ReleveCompte rejectReleve(UUID releveId, String reason) {
        ReleveCompte releveCompte = releveCompteRepository.findById(releveId).orElseThrow();
        CompteBancaire compteBancaire = releveCompte.getCompteBancaire();

        releveCompte.setStatut("REJECTED");
        releveCompte.setDeliveredBy(authenticationService.getConectedUser());
        releveCompte.setDateRejet(LocalDateTime.now());
        releveCompte.setCommentaire(reason);
        releveCompte = releveCompteRepository.save(releveCompte);

        // Send notification to client
        List<FcmToken> tokens = fcmTokenRepository.findByClient(compteBancaire.getClient());
        for (FcmToken token : tokens) {
            try{
                firebaseMessagingService.sendNotification(
                        token.getToken(),
                        "Rejet de relevé de compte",
                        "Nous vous informons que votre demande de relevé de compte a été rejeté. Veuillez vérifier les détails dans votre espace client."
                );
            } catch (Exception e) {
                System.out.println("Erreur lors de l'envoi de notification");
            }
        }
        return releveCompte;
    }

    @Override
    public List<ReleveCompteDto> findByCompteBancaireId(UUID id) {
        return releveCompteRepository.findByCompteBancaireId(id).stream()
                .map(releveCompte -> {
                    ReleveCompteDto dto = modelMapper.map(releveCompte, ReleveCompteDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReleveCompte> findAllFiltered(ReleveCompteSearch search) {
        System.out.println("Recherche de relevés filtrés");
        String statut = search.getStatut();
        String compte = search.getCompte();
        int page = search.getPage();
        int size = search.getSize();

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateSoumission").descending());
        if (statut != null && compte != null) {
            return releveCompteRepository.findByStatutAndCompte(statut, compte, pageable);
        } else if (statut != null) {
            System.out.println("Statut : " + statut);
            return releveCompteRepository.findByStatut(statut, pageable);
        } else if (compte != null) {
            System.out.println("ClientId : " + compte);
            return releveCompteRepository.findByCompte(compte, pageable);
        } else {
            System.out.println("Aucun filtre appliqué, récupération de tous les relevés");
            return releveCompteRepository.findAll(pageable);
        }
    }

}
