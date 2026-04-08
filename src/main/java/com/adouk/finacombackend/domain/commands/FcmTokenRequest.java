package com.adouk.finacombackend.domain.commands;

import lombok.Data;

import java.util.UUID;

@Data
public class FcmTokenRequest {
    private String token;
    private String brand;
    private String model;
    private String os;
    private String osVersion;
    private String uniqueId;
    private UUID clientId;
}
