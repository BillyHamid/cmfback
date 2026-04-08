package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.*;
import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.DocumentDestinator;
import com.adouk.finacombackend.domain.aggregates.DocumentRequis;
import com.adouk.finacombackend.domain.aggregates.FoirAQuestion;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public/mobile")
@AllArgsConstructor
public class PublicController {

    private final AgenceService agenceService;
    private final ClientService clientService;
    private final ProspectClientService prospectClientService;
    private final ModelMapper modelMapper;
    private final DocumentRequisService service;
    private final ServicesService serviceService;

    private final FoirAQuestionService foirAQuestionService;

    @GetMapping("/faq")
    public ResponseEntity<List<FaqDto>> getAll() {
        return ResponseEntity.ok(foirAQuestionService.findAll().stream().map(faq -> modelMapper.map(faq, FaqDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAll().stream().map(service -> modelMapper.map(service, ServiceDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/agences/all")
    public ResponseEntity<List<Agence>> findAll() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    @PostMapping("/client/physique/create")
    public ResponseEntity<ClientPhysiqueDto> creerClientPhysique(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientPhysiqueCommand clientPhysiqueCommand = objectMapper.readValue(command, ClientPhysiqueCommand.class);

        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) {
            clientPhysiqueCommand.setFichiersJustificatifs(files);
        }

        ClientPhysiqueDto nouveauClient = clientService.creerClientPhysique(clientPhysiqueCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }

    @PostMapping("/client/prospect/physique/create")
    public ResponseEntity<ProspectClientPhysiqueDto> creerProspectClientPhysique(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProspectClientPhysiqueCommand clientPhysiqueCommand = objectMapper.readValue(command, ProspectClientPhysiqueCommand.class);

        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) {
            clientPhysiqueCommand.setFichiersJustificatifs(files);
        }

        ProspectClientPhysiqueDto nouveauClient = prospectClientService.creerClientPhysique(clientPhysiqueCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }


    @PostMapping("/client/prospect/moral/create")
    public ResponseEntity<ProspectClientMoralDto> creerClientMoral(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProspectClientMoralCommand clientMoralCommand = objectMapper.readValue(command, ProspectClientMoralCommand.class);

        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) {
            clientMoralCommand.setFichiersJustificatifs(files);
        }

        ProspectClientMoralDto nouveauClient = prospectClientService.creerClientMoral(clientMoralCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }

    @PostMapping("/client/prospect/association/create")
    public ResponseEntity<ProspectClientAssociationDto> creerClientAssociation(
            @RequestPart("data") @Valid String command,
            MultipartHttpServletRequest request
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ProspectClientAssociationCommand clientCommand = objectMapper.readValue(command, ProspectClientAssociationCommand.class);

        Map<String, MultipartFile> files = request.getFileMap();
        if (files != null && !files.isEmpty()) {
            clientCommand.setFichiersJustificatifs(files);
        }
        ProspectClientAssociationDto nouveauClient = prospectClientService.creerClientAssociation(clientCommand);
        return ResponseEntity.status(201).body(nouveauClient);
    }


    @GetMapping("/documents-requis/by-destinator/{destinator}")
    public ResponseEntity<List<DocumentRequis>> getByDestinator(@PathVariable DocumentDestinator destinator) {
        return ResponseEntity.ok(service.getDocumentsByDestinator(destinator));
    }

}
