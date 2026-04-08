package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findByKycStatus(ClientStatus kycStatus);

    @Query("SELECT c FROM Client c WHERE c.telephone1 = :telephone1")
    Optional<Client> findByTelephone1(String telephone1);

    @Query("""
        SELECT c.agenceRattachee.agencyName AS agence, COUNT(c) AS total
        FROM Client c
        WHERE c.agenceRattachee IS NOT NULL
        GROUP BY c.agenceRattachee.agencyName
    """)
    List<ClientCountByAgenceProjection> countClientsByAgence();
}
