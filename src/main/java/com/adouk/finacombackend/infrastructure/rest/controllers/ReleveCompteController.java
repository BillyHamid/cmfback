package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.AgenceService;
import com.adouk.finacombackend.Application.services.Interfaces.ReleveCompteService;
import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.ReleveCompte;
import com.adouk.finacombackend.domain.commands.ReleveCommand;
import com.adouk.finacombackend.infrastructure.repositories.ReleveCompteRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.ReleveCompteDto;
import com.adouk.finacombackend.infrastructure.rest.dto.ReleveCompteSearch;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comptes-bancaires/releves")
@AllArgsConstructor
public class ReleveCompteController {

    private final ReleveCompteService releveCompteService;
    private final ReleveCompteRepository releveCompteRepository;

//    @GetMapping("")
    public ResponseEntity<List<ReleveCompte>> findAll() {
        return ResponseEntity.ok(releveCompteService.findAll());
    }
    @PostMapping("search")
    public Page<ReleveCompte> findAllFiltered(@RequestBody ReleveCompteSearch search) {
        return releveCompteService.findAllFiltered(search);
    }


    @PostMapping("")
    public ResponseEntity<ReleveCompte> create(@RequestBody ReleveCommand releveCompteDto) {
        return ResponseEntity.ok(releveCompteService.create(releveCompteDto));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ReleveCompte> submitReleve(@PathVariable UUID id, @RequestParam MultipartFile file) {
        return ResponseEntity.ok(releveCompteService.submitReleve(id, file));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ReleveCompte> rejectReleve(@PathVariable UUID id, @RequestParam String reason) {
        return ResponseEntity.ok(releveCompteService.rejectReleve(id, reason));
    }

    @GetMapping("/by-comptes/{id}")
    public ResponseEntity<List<ReleveCompteDto>> findByCompteBancaireId(@PathVariable UUID id) {
        return ResponseEntity.ok(releveCompteService.findByCompteBancaireId(id));
    }


}
