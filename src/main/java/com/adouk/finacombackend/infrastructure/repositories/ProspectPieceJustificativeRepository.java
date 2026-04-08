package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.PieceJustificative;
import com.adouk.finacombackend.domain.aggregates.ProspecrPieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProspectPieceJustificativeRepository extends JpaRepository<ProspecrPieceJustificative, UUID> {

    @Query("select pj from ProspecrPieceJustificative pj where pj.client.id = :clientId")
    List<ProspecrPieceJustificative> findByProspect(UUID clientId);
}

