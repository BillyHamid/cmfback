package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.ClientMoral;
import com.adouk.finacombackend.domain.aggregates.ClientPhysique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClientMoralRepository extends JpaRepository<ClientMoral, UUID>, JpaSpecificationExecutor<ClientMoral> {
}
