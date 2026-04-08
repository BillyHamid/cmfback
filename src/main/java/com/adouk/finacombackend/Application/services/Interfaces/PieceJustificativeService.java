package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.Mandataire;
import com.adouk.finacombackend.domain.aggregates.PieceJustificative;
import com.adouk.finacombackend.domain.valueObjects.TypePiece;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PieceJustificativeService {

    PieceJustificativeDto createPiece(String type, String description, MultipartFile file, Client client, Mandataire mandataire);

    List<PieceJustificativeDto> getByClient(UUID client);
    List<PieceJustificativeDto> getByMandataire(UUID mandataire);
}
