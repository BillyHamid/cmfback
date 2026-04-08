package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.DocumentDestinator;
import com.adouk.finacombackend.domain.aggregates.DocumentRequis;
import com.adouk.finacombackend.infrastructure.rest.dto.DocumentRequisDto;

import java.util.List;
import java.util.UUID;

public interface DocumentRequisService {
    DocumentRequis createDocument(DocumentRequisDto dto);
    List<DocumentRequis> getDocumentsByDestinator(DocumentDestinator destinator);
    void deleteDocument(UUID uuid);

}
