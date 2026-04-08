package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

@Data
public class AuthWith2FAResponse {

    private boolean requires2fa;
    private String message;
}
