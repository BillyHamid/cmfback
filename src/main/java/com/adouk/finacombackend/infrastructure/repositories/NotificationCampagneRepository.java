package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.FcmToken;
import com.adouk.finacombackend.domain.aggregates.NotificationCampagne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationCampagneRepository extends JpaRepository<NotificationCampagne, UUID> {
}
