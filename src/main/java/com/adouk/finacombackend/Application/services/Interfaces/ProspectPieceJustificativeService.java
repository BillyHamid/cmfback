package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.ProspectClient;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProspectPieceJustificativeService {

    PieceJustificativeDto createPiece(String type, String description, MultipartFile file, ProspectClient client);

    List<PieceJustificativeDto> getByClient(UUID client);
}
