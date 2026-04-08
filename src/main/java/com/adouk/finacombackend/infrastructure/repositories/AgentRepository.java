package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {
}
