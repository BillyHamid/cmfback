package com.adouk.finacombackend.Application.services.Impl;
import com.adouk.finacombackend.Application.services.Interfaces.DocumentRequisService;
import com.adouk.finacombackend.domain.aggregates.DocumentDestinator;
import com.adouk.finacombackend.domain.aggregates.DocumentRequis;
import com.adouk.finacombackend.infrastructure.repositories.DocumentRequisRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.DocumentRequisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentRequisServiceImpl implements DocumentRequisService {

    private final DocumentRequisRepository repository;

    @Override
    public DocumentRequis createDocument(DocumentRequisDto dto) {
        DocumentRequis doc = new DocumentRequis();
        doc.setLibelle(dto.getLibelle());
        doc.setDescription(dto.getDescription());
        doc.setType(dto.getType());
        doc.setDestinator(dto.getDestinator());
        return repository.save(doc);
    }

    @Override
    public List<DocumentRequis> getDocumentsByDestinator(DocumentDestinator destinator) {
        return repository.findByDestinator(destinator);
    }

    @Override
    public void deleteDocument(UUID uuid) {
        repository.deleteById(uuid);
    }
}
