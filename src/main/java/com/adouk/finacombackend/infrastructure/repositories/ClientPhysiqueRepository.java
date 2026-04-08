package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.ClientPhysique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClientPhysiqueRepository extends JpaRepository<ClientPhysique, UUID>, JpaSpecificationExecutor<ClientPhysique> {
}
