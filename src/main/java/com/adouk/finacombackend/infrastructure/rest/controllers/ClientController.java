package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.ClientService;
import com.adouk.finacombackend.Application.services.Interfaces.DossierClientService;
import com.adouk.finacombackend.Application.services.Interfaces.ProspectClientService;
import com.adouk.finacombackend.domain.aggregates.ClientType;
import com.adouk.finacombackend.domain.aggregates.DossierClient;
import com.adouk.finacombackend.domain.commands.*;
import com.adouk.finacombackend.infrastructure.rest.dto.*;
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
@RequestMapping("/api/v1/admin/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final ProspectClientService prospectClientService;
    private final DossierClientService dossierClientService;

    @PostMapping("/physique/create")
    public ResponseEntity<ClientPhysiqueDto> creerClientPhysique(
            @RequestPart("data") @Valid String command,
            @RequestParam Map<String, MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientPhysiqueCommand clientPhysiqueCommand = objectMapper.readValue(command, ClientPhysiqueCommand.class);

        // Passage des fichiers dynamiques
        clientPhysiqueCommand.setFichiersJustificatifs(files);

        ClientPhysiqueDto nouveauClient = clientService.creerClientPhysique(clientPhysiqueCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }

    @PostMapping("/physique/update/{id}")
    public ResponseEntity<ClientPhysiqueDto> updateClientPhysique(
            @RequestPart("data") @Valid String command,
            @PathVariable String id,
            @RequestParam Map<String, MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientPhysiqueCommand clientPhysiqueCommand = objectMapper.readValue(command, ClientPhysiqueCommand.class);

        // Passage des fichiers dynamiques
        clientPhysiqueCommand.setFichiersJustificatifs(files);

        ClientPhysiqueDto nouveauClient = clientService.updateClientPhysique(UUID.fromString(id), clientPhysiqueCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }




    @PostMapping("/physique/search")
    public ResponseEntity<Page<ClientPhysiqueDto>> rechercherClients(
            @RequestBody ClientFilterCommand command,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ClientPhysiqueDto> result = clientService.rechercherClientsPhysique(command, pageable);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/moral/create")
    public ResponseEntity<ClientMoralDto> creerClientMoral(
            @RequestPart("data") @Valid String command,
            @RequestParam Map<String, MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientMoralCommand clientMoralCommand = objectMapper.readValue(command, ClientMoralCommand.class);

        clientMoralCommand.setFichiersJustificatifs(files); // méthode à créer dans ClientMoralCommand

        ClientMoralDto nouveauClient = clientService.creerClientMoral(clientMoralCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }

    @PostMapping("/moral/update/{id}")
    public ResponseEntity<ClientMoralDto> updateClientMoral(
            @RequestPart("data") @Valid String command,
            @PathVariable String id,
            @RequestParam Map<String, MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientMoralCommand clientMoralCommand = objectMapper.readValue(command, ClientMoralCommand.class);

        // Passage des fichiers dynamiques
        clientMoralCommand.setFichiersJustificatifs(files);

        ClientMoralDto nouveauClient = clientService.updateClientMoral(UUID.fromString(id), clientMoralCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }

    @PostMapping("/association/update/{id}")
    public ResponseEntity<ClientAssociationDto> updateClientAssociation(
            @RequestPart("data") @Valid String command,
            @PathVariable String id,
            @RequestParam Map<String, MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientAssociationCommand clientMoralCommand = objectMapper.readValue(command, ClientAssociationCommand.class);

        // Passage des fichiers dynamiques
        clientMoralCommand.setFichiersJustificatifs(files);

        ClientAssociationDto nouveauClient = clientService.updateClientAssociation(UUID.fromString(id), clientMoralCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }


    @PostMapping("/moral/search")
    public ResponseEntity<Page<ClientMoralDto>> rechercherClientsMoral(
            @RequestBody ClientFilterCommand command,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "raisonSociale") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ClientMoralDto> result = clientService.rechercherClientsMoral(command, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/association/create")
    public ResponseEntity<ClientAssociationDto> creerClientAssociation(
            @RequestPart("data") @Valid String command,
            @RequestParam Map<String, MultipartFile> files
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClientAssociationCommand clientCommand = objectMapper.readValue(command, ClientAssociationCommand.class);

        clientCommand.setFichiersJustificatifs(files);
        ClientAssociationDto nouveauClient = clientService.creerClientAssociation(clientCommand);
        return ResponseEntity.status(201).body(nouveauClient);
    }


    @PostMapping("/association/search")
    public ResponseEntity<Page<ClientAssociationDto>> rechercherClientsAssociation(
            @RequestBody ClientFilterCommand command,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nomOrganisation") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ClientAssociationDto> result = clientService.rechercherClientsAssociation(command, pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/dossiers/validate")
    public ResponseEntity<DossierClient> validateKyc(@RequestBody ValidationDossierCommand command) {
        return ResponseEntity.ok(dossierClientService.validateKyc(command));
    }

    @PutMapping("/dossiers/reject")
    public ResponseEntity<DossierClient> validateKycAll(@RequestBody ValidationDossierCommand command) {
        return ResponseEntity.ok(dossierClientService.rejectedKyc(command));
    }

    @GetMapping("/dossiers/pending")
    public ResponseEntity<List<DossierClient>> getPending() {
        return ResponseEntity.ok(dossierClientService.getPending());
    }

    @GetMapping("/dossiers/traite")
    public ResponseEntity<List<DossierClient>> getTraite() {
        return ResponseEntity.ok(dossierClientService.getTraiter());
    }

    @GetMapping("/dossiers/{id}")
    public ResponseEntity<Object> getDossierDetails(@PathVariable UUID id) {
        DossierClient byId = dossierClientService.getById(id);
        if (byId.getTypeClient() == ClientType.PHYSIQUE) {
            return ResponseEntity.ok(prospectClientService.findClientPhysiqueById(byId.getProspectClient().getId()));
        }
        if (byId.getTypeClient() == ClientType.MORALE) {
            return ResponseEntity.ok(prospectClientService.findClientMoralById(byId.getProspectClient().getId()));
        }
        if (byId.getTypeClient() == ClientType.ASSOCIATION) {
            return ResponseEntity.ok(prospectClientService.findClientAssociationById(byId.getProspectClient().getId()));
        }
        return null;
    }

    @GetMapping("/current")
    public ResponseEntity<Object> getCurrentClient() {
        return ResponseEntity.ok(clientService.getCurrentClient());
    }

    @GetMapping("/current/gestionnaire")
    public ResponseEntity<GestionnaireDto> getCurrentClientGestionnaire() {
        return ResponseEntity.ok(clientService.getCurrentClientGestionnaire());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteClient(@PathVariable String id) {
        return ResponseEntity.ok(clientService.deleteClient(UUID.fromString(id)));
    }

    @PostMapping("/{id}/enable-mobile")
    public ResponseEntity<ClientDto> enableMobileAccount(@PathVariable String id) {
        return ResponseEntity.ok(clientService.enableMobileAccount(UUID.fromString(id)));
    }
}
