package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.DocumentDestinator;
import lombok.Data;

@Data
public class DocumentRequisDto {
    private String libelle;
    private String description;
    private String type;
    private DocumentDestinator destinator;
}
