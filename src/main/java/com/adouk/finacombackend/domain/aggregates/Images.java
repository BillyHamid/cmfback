package com.adouk.finacombackend.domain.aggregates;


import com.adouk.finacombackend.domain.valueObjects.Audit;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Images extends Audit {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private UUID imageId;

    @Column(name = "modele")
    private String modele;
    @Column(name = "modeleId")
    private UUID modeleId;
    @Column(name = "path")
    private String path;
}
