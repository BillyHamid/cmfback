package com.adouk.finacombackend.infrastructure.rest.dto;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.domain.valueObjects.TypePiece;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Data
public class PieceJustificativeDto {

    private UUID uuid;
    private String libelle;
    private TypePiece typePiece;
    private String extension;
    private String dateEnregistrement;
    private String urlFichier;

    @ManyToOne
    private User uploader;

}
