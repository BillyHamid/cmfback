package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.ClientAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClientAssociationRepository extends JpaRepository<ClientAssociation, UUID>, JpaSpecificationExecutor<ClientAssociation> {
}

