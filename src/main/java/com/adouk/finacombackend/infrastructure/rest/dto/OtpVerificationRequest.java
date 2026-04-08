package com.adouk.finacombackend.infrastructure.rest.dto;


import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String phone;
    private String code;
}

