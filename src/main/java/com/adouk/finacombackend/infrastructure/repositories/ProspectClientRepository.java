package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.ProspectClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProspectClientRepository extends JpaRepository<ProspectClient, UUID> {
}
