package com.adouk.finacombackend.infrastructure.rest.controllers;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.configs.TotpUtil;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthWith2FAResponse;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthenticationRequest;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthenticationResponse;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/admin/login")
    public AuthenticationResponse auth(@RequestBody AuthenticationRequest request) {
        return authenticationService.authentificateAdmin(request);
    }

    @PostMapping("/admin/reset-password/{email}")
    public Map<String, String> resetPass(@PathVariable String email) {
        return authenticationService.resetPass(email);
    }

    @PostMapping("/client/login")
    public AuthenticationResponse clientAuth(@RequestBody AuthenticationRequest request) {
        return authenticationService.authentificateClient(request);
    }

//    @GetMapping("/generate-qr-code")
//    public ResponseEntity<byte[]> generateQRCode() throws IOException, WriterException {
//        return ResponseEntity.ok(TotpUtil.generateQRCode("FINACOM+", "ezechiaskouda@gmail.com", null));
//    }
//
//    @GetMapping("/validate")
//    public ResponseEntity<Boolean> validate(@RequestParam int code) {
//        return ResponseEntity.ok(TotpUtil.validateCode("66XT4C7VVTT6TMIFBTWZELZ2IIO7GYIT", code));
//    }

}
