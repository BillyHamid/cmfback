package com.adouk.finacombackend.domain.aggregates;

import com.adouk.finacombackend.domain.valueObjects.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "document_requis")
public class DocumentRequis extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String libelle;
    private String description;
    private String type;
    @Enumerated(EnumType.STRING)
    private DocumentDestinator destinator;
}
