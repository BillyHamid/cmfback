package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.NotificationCampagne;

import java.util.UUID;

public interface NotificationSenderService {
    void envoyerCampagne(NotificationCampagne campagne);
    void delete(UUID id);
}
