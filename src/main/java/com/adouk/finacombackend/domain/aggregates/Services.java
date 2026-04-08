package com.adouk.finacombackend.domain.aggregates;

import com.adouk.finacombackend.domain.valueObjects.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "services")
@Getter
@Setter
public class Services extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String code;
    private String libelleService;
    private String descriptionService;
}
