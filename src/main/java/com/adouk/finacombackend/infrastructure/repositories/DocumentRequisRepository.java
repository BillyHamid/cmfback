package com.adouk.finacombackend.infrastructure.repositories;
import com.adouk.finacombackend.domain.aggregates.DocumentDestinator;
import com.adouk.finacombackend.domain.aggregates.DocumentRequis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRequisRepository extends JpaRepository<DocumentRequis, UUID> {
    List<DocumentRequis> findByDestinator(DocumentDestinator destinator);
}
