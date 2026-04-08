package com.adouk.finacombackend.domain.aggregates;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class NotificationCampagne {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String titre;
    private String message;
    private LocalDateTime dateEnvoi;
    private Boolean envoyee = false;
    private Boolean programmee = false;

    @Enumerated(EnumType.STRING)
    private CibleNotification cible;

    @ManyToOne
    private User createdBy;
}

