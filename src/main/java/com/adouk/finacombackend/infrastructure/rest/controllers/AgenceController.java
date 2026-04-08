package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.AgenceService;
import com.adouk.finacombackend.Application.services.Interfaces.UserSessionQueryService;
import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.commands.AgenceCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agences")
@AllArgsConstructor
public class AgenceController {

    private final AgenceService agenceService;

    @GetMapping("")
    public ResponseEntity<List<Agence>> findAll() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    @GetMapping("/toggle-status/{id}")
    public ResponseEntity<Agence> toggleStatus(@PathVariable String id) {
        return ResponseEntity.ok(agenceService.toggleStatus(UUID.fromString(id)));
    }

    @PostMapping("")
    public ResponseEntity<Agence> create(@RequestBody AgenceCommand agence) {
        return ResponseEntity.ok(agenceService.create(agence));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Agence> update(@PathVariable String id, @RequestBody AgenceCommand agence) {
        return ResponseEntity.ok(agenceService.update(UUID.fromString(id), agence));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        agenceService.delete(UUID.fromString(id));
        return ResponseEntity.ok(Map.of("message", "Agence supprimée"));
    }
}
