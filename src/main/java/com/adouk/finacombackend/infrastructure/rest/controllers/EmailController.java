package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String message
    ) {
        String html = """
                <html>
                <body>
                    <h2 style="color: #247039;">%s</h2>
                    <p>%s</p>
                </body>
                </html>
                """.formatted(subject, message);

        emailService.sendHtmlEmail(to, subject, html);
        return ResponseEntity.ok("Email envoyé à " + to);
    }
}

