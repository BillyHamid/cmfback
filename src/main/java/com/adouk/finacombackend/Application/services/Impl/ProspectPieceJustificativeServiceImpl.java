package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.FileStorageService;
import com.adouk.finacombackend.Application.services.Interfaces.PieceJustificativeService;
import com.adouk.finacombackend.Application.services.Interfaces.ProspectPieceJustificativeService;
import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.PieceJustificative;
import com.adouk.finacombackend.domain.aggregates.ProspecrPieceJustificative;
import com.adouk.finacombackend.domain.aggregates.ProspectClient;
import com.adouk.finacombackend.infrastructure.repositories.PieceJustificativeRepository;
import com.adouk.finacombackend.infrastructure.repositories.ProspectPieceJustificativeRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProspectPieceJustificativeServiceImpl implements ProspectPieceJustificativeService {

    private final FileStorageService fileStorageService;
    private final ProspectPieceJustificativeRepository pieceJustificativeRepository;
    private final ModelMapper modelMapper;

    @Override
    public PieceJustificativeDto createPiece(String type, String description, MultipartFile file, ProspectClient client) {
        String url = fileStorageService.storeToS3(client.getNumeroClient().concat("/").concat("KYC"), type.toLowerCase(), file);
        ProspecrPieceJustificative piece = new ProspecrPieceJustificative();
        piece.setUrlFichier(url);
        piece.setLibelle(type);
        piece.setClient(client);
        piece.setExtension(file.getContentType());
        piece.setDateEnregistrement(LocalDateTime.now());
        piece = pieceJustificativeRepository.save(piece);
        return modelMapper.map(piece, PieceJustificativeDto.class);
    }

    @Override
    public List<PieceJustificativeDto> getByClient(UUID client) {
        return pieceJustificativeRepository.findByProspect(client).stream()
                .map(piece -> modelMapper.map(piece, PieceJustificativeDto.class))
                .collect(Collectors.toList());
    }
}
