package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.UserSessionQueryService;
import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-session")
@AllArgsConstructor
public class UserSessionController {

    private final UserSessionQueryService userSessionQueryService;

    @GetMapping("")
    public ResponseEntity<List<UserSessionDto>> findAll() {
        return ResponseEntity.ok(userSessionQueryService.findAll());
    }
}
