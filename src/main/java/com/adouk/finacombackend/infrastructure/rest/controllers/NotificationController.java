package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Impl.FirebaseMessagingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final FirebaseMessagingService firebaseService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody NotificationRequest request) {

        return ResponseEntity.ok(firebaseService.sendNotification(request.getToken(), request.getTitle(), request.getBody()));
    }
}

@Data
class NotificationRequest {
    private String token;
    private String title;
    private String body;
}
