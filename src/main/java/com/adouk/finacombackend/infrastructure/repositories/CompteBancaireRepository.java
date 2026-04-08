package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, UUID> {
    List<CompteBancaire> findAllByClient(Client client);
}
