package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.ClientMoral;
import com.adouk.finacombackend.domain.aggregates.ProspectClientMoral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProspectClientMoralRepository extends JpaRepository<ProspectClientMoral, UUID>, JpaSpecificationExecutor<ProspectClientMoral> {
}
