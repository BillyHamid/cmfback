package com.adouk.finacombackend.domain.aggregates;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
public class UserSession {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private UUID userSessionId;

    @ManyToOne
    private User user;
    private Date startDate;
    private Date endDate;
    private String ipAdress;
    private String location;
    private boolean statut;
    private String token;

    public UserSession() {

    }
}
