package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, UUID> {
    /**
     * Find all PieceJustificative entities associated with a specific client ID.
     *
     * @param clientId the ID of the client
     * @return a list of PieceJustificative entities associated with the specified client ID
     */
    List<PieceJustificative> findByClientId(UUID clientId);
    List<PieceJustificative> findByMandataireId(UUID clientId);
}

