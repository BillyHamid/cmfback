package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, UUID> {
    Optional<FcmToken> findByToken(String token);

    @Query("select f from FcmToken f where f.client.class = 'PHYSIQUE'")
    List<FcmToken> findByClientPhysique();

    @Query("select f from FcmToken f where f.client.class = 'MORAL'")
    List<FcmToken> findByClientMorale();

    @Query("select f from FcmToken f where f.client.class = 'ASSOCIATION'")
    List<FcmToken> findByClientAsso();

    List<FcmToken> findByClient(Client client);
}
