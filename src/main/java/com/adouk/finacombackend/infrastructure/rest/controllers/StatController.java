package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.AgenceService;
import com.adouk.finacombackend.Application.services.Interfaces.ClientService;
import com.adouk.finacombackend.Application.services.Interfaces.StatistiqueService;
import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.infrastructure.repositories.ClientCountByAgenceProjection;
import com.adouk.finacombackend.infrastructure.rest.dto.ClientCountByTypeDto;
import com.adouk.finacombackend.infrastructure.rest.dto.CountDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/stats")
@AllArgsConstructor
public class StatController {

    private final StatistiqueService statistiqueService;
    private final ClientService clientService;

    @GetMapping("/dossiers-count")
    public ResponseEntity<CountDto> getDossierCount() {
        return ResponseEntity.ok(statistiqueService.getDossierCount());
    }

    @GetMapping("/clients-count")
    public ResponseEntity<CountDto> getClientCount() {
        return ResponseEntity.ok(statistiqueService.getClientCount());
    }

    @GetMapping("/clients-count-by-type")
    public ResponseEntity<ClientCountByTypeDto> getClientCountByType() {
        return ResponseEntity.ok(statistiqueService.getCLientCountByType());
    }

    @GetMapping("/agences-count")
    public ResponseEntity<List<ClientCountByAgenceProjection>> getAgenceCount() {
        return ResponseEntity.ok(clientService.getClientCountByAgence());
    }



}
