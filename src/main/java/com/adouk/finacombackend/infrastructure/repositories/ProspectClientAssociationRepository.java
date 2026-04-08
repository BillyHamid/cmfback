package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.ClientAssociation;
import com.adouk.finacombackend.domain.aggregates.ProspectClientAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProspectClientAssociationRepository extends JpaRepository<ProspectClientAssociation, UUID>, JpaSpecificationExecutor<ProspectClientAssociation> {
}

