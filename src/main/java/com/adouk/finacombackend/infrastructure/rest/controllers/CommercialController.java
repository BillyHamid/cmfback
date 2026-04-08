package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.CommercialService;
import com.adouk.finacombackend.domain.aggregates.DossierClient;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/commercial")
@RequiredArgsConstructor
public class CommercialController {

    private final CommercialService commercialService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/prospects")
    public ResponseEntity<List<DossierClient>> getMyProspects() {
        return ResponseEntity.ok(commercialService.getMyProspects());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboard() {
        return ResponseEntity.ok(commercialService.getDashboardStats());
    }

    @PostMapping("/prospects/physique")
    public ResponseEntity<ProspectClientPhysiqueDto> creerProspectPhysique(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        ProspectClientPhysiqueCommand cmd = objectMapper.readValue(command, ProspectClientPhysiqueCommand.class);
        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) cmd.setFichiersJustificatifs(files);
        return ResponseEntity.status(HttpStatus.CREATED).body(commercialService.creerProspectPhysique(cmd));
    }

    @PostMapping("/prospects/moral")
    public ResponseEntity<ProspectClientMoralDto> creerProspectMoral(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        ProspectClientMoralCommand cmd = objectMapper.readValue(command, ProspectClientMoralCommand.class);
        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) cmd.setFichiersJustificatifs(files);
        return ResponseEntity.status(HttpStatus.CREATED).body(commercialService.creerProspectMoral(cmd));
    }

    @PostMapping("/prospects/association")
    public ResponseEntity<ProspectClientAssociationDto> creerProspectAssociation(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        ProspectClientAssociationCommand cmd = objectMapper.readValue(command, ProspectClientAssociationCommand.class);
        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) cmd.setFichiersJustificatifs(files);
        return ResponseEntity.status(HttpStatus.CREATED).body(commercialService.creerProspectAssociation(cmd));
    }

    @GetMapping("/prospects/{dossierId}")
    public ResponseEntity<Object> getProspectDetail(@PathVariable java.util.UUID dossierId) {
        return ResponseEntity.ok(commercialService.getProspectDetail(dossierId));
    }
}
