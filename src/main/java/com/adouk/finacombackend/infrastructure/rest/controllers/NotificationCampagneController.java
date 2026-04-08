package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.NotificationSenderService;
import com.adouk.finacombackend.domain.aggregates.NotificationCampagne;
import com.adouk.finacombackend.infrastructure.repositories.NotificationCampagneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/campagnes")
@RequiredArgsConstructor
public class NotificationCampagneController {

    private final NotificationCampagneRepository campagneRepo;
    private final NotificationSenderService senderService;

    @PostMapping
    public ResponseEntity<NotificationCampagne> creerEtEnvoyer(@RequestBody NotificationCampagne campagne) {
        campagne = campagneRepo.save(campagne);
        if (!campagne.getProgrammee()) {
            senderService.envoyerCampagne(campagne);
        }
        campagne = campagneRepo.save(campagne);
        return ResponseEntity.ok(campagne);
    }

    @GetMapping
    public List<NotificationCampagne> list() {
        return campagneRepo.findAll(Sort.by(Sort.Direction.DESC, "dateEnvoi"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        senderService.delete(id);
        return ResponseEntity.ok().build();
    }
}

