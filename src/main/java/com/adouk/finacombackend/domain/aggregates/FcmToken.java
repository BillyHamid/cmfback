package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String token;

    private String brand;
    private String model;
    private String os;
    private String osVersion;
    private String uniqueId;

    @ManyToOne
    private Client client;

    private LocalDateTime createdAt = LocalDateTime.now();
}

