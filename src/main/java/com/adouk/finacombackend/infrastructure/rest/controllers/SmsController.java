package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.services.Impl.OtpService;
import com.adouk.finacombackend.Application.services.Impl.SmsService;
import com.adouk.finacombackend.infrastructure.rest.dto.OtpVerificationRequest;
import com.adouk.finacombackend.infrastructure.rest.dto.SmsRequest;
import com.adouk.finacombackend.infrastructure.rest.dto.SmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/otp")
public class SmsController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send/{otp}")
    public ResponseEntity<SmsResponse> sendOtp(@PathVariable String otp) {
        return ResponseEntity.ok(otpService.sendOtp(otp));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody OtpVerificationRequest request) {
        System.out.println(request);
        try {
            String result = otpService.verifyOtp(request);
            return switch (result) {
                case "OTP vérifié avec succès" -> ResponseEntity.ok(result); // 200 OK

                case "Déjà vérifié" -> ResponseEntity.status(HttpStatus.CONFLICT).body(result); // 409 Conflict

                case "OTP incorrect" -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result); // 401 Unauthorized

                case "OTP expiré" -> ResponseEntity.status(HttpStatus.GONE).body(result); // 410 Gone

                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requête invalide");
            };
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP non trouvé");
        }
    }

}

