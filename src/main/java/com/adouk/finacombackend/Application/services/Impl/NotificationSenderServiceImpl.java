package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.NotificationSenderService;
import com.adouk.finacombackend.domain.aggregates.FcmToken;
import com.adouk.finacombackend.domain.aggregates.NotificationCampagne;
import com.adouk.finacombackend.infrastructure.repositories.FcmTokenRepository;
import com.adouk.finacombackend.infrastructure.repositories.NotificationCampagneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationSenderServiceImpl implements NotificationSenderService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final NotificationCampagneRepository notificationCampagneRepository;

    @Override
    public void envoyerCampagne(NotificationCampagne campagne) {
        List<FcmToken> tokens = switch (campagne.getCible()) {
            case TOUS -> fcmTokenRepository.findAll();
            case CLIENT_PHYSIQUE -> fcmTokenRepository.findByClientPhysique();
            case CLIENT_MORALE -> fcmTokenRepository.findByClientMorale();
            case CLIENT_ASSOCIATION -> fcmTokenRepository.findByClientAsso();
            default -> new ArrayList<>();
        };

        if (!campagne.getProgrammee()){
            for (FcmToken token : tokens) {
                if (token.getToken() != null) {
                    System.out.println(firebaseMessagingService.sendNotification(
                            token.getToken(),
                            campagne.getTitre(),
                            campagne.getMessage()
                    ));
                }
            }
        }

        campagne.setDateEnvoi(LocalDateTime.now());
        campagne.setEnvoyee(true);
    }

    @Override
    public void delete(UUID id) {
       notificationCampagneRepository.deleteById(id);
    }
}

