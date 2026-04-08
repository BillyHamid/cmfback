package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.ServicesService;
import com.adouk.finacombackend.domain.aggregates.Services;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
@AllArgsConstructor
public class ServicesController {

    private final ServicesService service;

    @GetMapping("")
    public ResponseEntity<List<Services>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("")
    public ResponseEntity<Services> create(@RequestBody Services faq) {
        return ResponseEntity.ok(service.create(faq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> update(@PathVariable String id, @RequestBody Services faq) {
        return ResponseEntity.ok(service.update(UUID.fromString(id), faq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.ok(Map.of("message", "FAQ supprimée"));
    }
}
