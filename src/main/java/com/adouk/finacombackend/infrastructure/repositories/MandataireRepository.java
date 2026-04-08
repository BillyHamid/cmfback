package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.CompteBancaire;
import com.adouk.finacombackend.domain.aggregates.Mandataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MandataireRepository extends JpaRepository<Mandataire, UUID> {

    @Query("select m from Mandataire m where m.compteBancaire.id = :id")
    List<Mandataire> findByCompteBancaireId(UUID id);
}
