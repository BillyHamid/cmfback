package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.ClientService;
import com.adouk.finacombackend.Application.services.Interfaces.CompteBancaireService;
import com.adouk.finacombackend.Application.services.Interfaces.DossierClientService;
import com.adouk.finacombackend.Application.services.Interfaces.ProspectClientService;
import com.adouk.finacombackend.domain.aggregates.ClientType;
import com.adouk.finacombackend.domain.aggregates.DossierClient;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.rest.dto.ClientAssociationDto;
import com.adouk.finacombackend.infrastructure.rest.dto.ClientMoralDto;
import com.adouk.finacombackend.infrastructure.rest.dto.ClientPhysiqueDto;
import com.adouk.finacombackend.infrastructure.rest.dto.CompteBancaireDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/comptes")
@RequiredArgsConstructor
public class CompteBancaireController {

    private final CompteBancaireService compteBancaireService;

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompteBancaireDto> creerCompte(
            @RequestPart("command") CompteBancaireCommand command,
            @RequestParam Map<String, MultipartFile> fichiers
    ) {
        CompteBancaireDto dto = compteBancaireService.creerCompte(command, fichiers);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompteBancaireDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(compteBancaireService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CompteBancaireDto>> getClientComptes(@PathVariable UUID clientId) {
        return ResponseEntity.ok(compteBancaireService.getClientComptes(clientId));
    }

    @GetMapping("/current-client")
    public ResponseEntity<List<CompteBancaireDto>> getCurrentClientComptes() {
        return ResponseEntity.ok(compteBancaireService.getCurrentClientComptes());
    }
}
