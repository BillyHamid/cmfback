package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.FoirAQuestionService;
import com.adouk.finacombackend.domain.aggregates.FoirAQuestion;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/faq")
@AllArgsConstructor
public class FoirAQuestionController {

    private final FoirAQuestionService service;

    @GetMapping("")
    public ResponseEntity<List<FoirAQuestion>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("")
    public ResponseEntity<FoirAQuestion> create(@RequestBody FoirAQuestion faq) {
        return ResponseEntity.ok(service.create(faq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoirAQuestion> update(@PathVariable String id, @RequestBody FoirAQuestion faq) {
        return ResponseEntity.ok(service.update(UUID.fromString(id), faq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.ok(Map.of("message", "FAQ supprimée"));
    }
}
