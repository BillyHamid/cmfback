package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.Application.services.Interfaces.UserQueryService;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.domain.commands.UserPasswordUpdateCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userServiceC;
    private final UserQueryService userQueryService;

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserCommand userCommand) {
        return ResponseEntity.ok(userServiceC.createAgent(userCommand));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserCommand userCommand, @PathVariable String id) {
        return ResponseEntity.ok(userServiceC.update(userCommand, UUID.fromString(id)));
    }
    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<UserDto> enableUser(@PathVariable String id) {
        return ResponseEntity.ok(userServiceC.enableUser(UUID.fromString(id)));
    }

    @PutMapping("/reinitialize-password")
    public ResponseEntity<UserDto> reinitializePassword(@RequestBody UserPasswordUpdateCommand command,
                                                        Principal principal) {
        System.out.println("Reinitialize password for user " + principal.getName());
        return ResponseEntity.ok(userServiceC.reinitializePassword(command, principal));
    }


    @GetMapping("")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userQueryService.findAllAgents());
    }

}
