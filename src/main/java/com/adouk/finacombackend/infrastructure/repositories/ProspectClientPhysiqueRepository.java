package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.ClientPhysique;
import com.adouk.finacombackend.domain.aggregates.ProspectClientPhysique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProspectClientPhysiqueRepository extends JpaRepository<ProspectClientPhysique, UUID>, JpaSpecificationExecutor<ProspectClientPhysique> {
}
