package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.DocumentRequisService;
import com.adouk.finacombackend.domain.aggregates.DocumentDestinator;
import com.adouk.finacombackend.domain.aggregates.DocumentRequis;
import com.adouk.finacombackend.infrastructure.rest.dto.DocumentRequisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents-requis")
@RequiredArgsConstructor
public class DocumentRequisController {

    private final DocumentRequisService service;

    @PostMapping
    public ResponseEntity<DocumentRequis> create(@RequestBody DocumentRequisDto dto) {
        return ResponseEntity.ok(service.createDocument(dto));
    }

    @GetMapping("/by-destinator/{destinator}")
    public ResponseEntity<List<DocumentRequis>> getByDestinator(@PathVariable DocumentDestinator destinator) {
        return ResponseEntity.ok(service.getDocumentsByDestinator(destinator));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        service.deleteDocument(uuid);
        return ResponseEntity.noContent().build();
    }

}
