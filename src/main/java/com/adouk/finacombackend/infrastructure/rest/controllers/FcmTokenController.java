package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.FcmTokenService;
import com.adouk.finacombackend.domain.commands.FcmTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerToken(@RequestBody FcmTokenRequest request) {
        tokenService.saveOrUpdateToken(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<Void> unregisterToken(@RequestParam String token) {
        tokenService.removeToken(token);
        return ResponseEntity.ok().build();
    }
}

