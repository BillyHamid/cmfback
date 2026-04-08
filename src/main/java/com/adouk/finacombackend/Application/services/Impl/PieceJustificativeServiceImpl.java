package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.FileStorageService;
import com.adouk.finacombackend.Application.services.Interfaces.PieceJustificativeService;
import com.adouk.finacombackend.domain.aggregates.Client;
import com.adouk.finacombackend.domain.aggregates.Mandataire;
import com.adouk.finacombackend.domain.aggregates.PieceJustificative;
import com.adouk.finacombackend.domain.valueObjects.TypePiece;
import com.adouk.finacombackend.infrastructure.repositories.PieceJustificativeRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.PieceJustificativeDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PieceJustificativeServiceImpl implements PieceJustificativeService {

    private final FileStorageService fileStorageService;
    private final PieceJustificativeRepository pieceJustificativeRepository;
    private final ModelMapper modelMapper;

    @Override
    public PieceJustificativeDto createPiece(String type, String description, MultipartFile file, Client client, Mandataire mandataire) {
        String url = "";
        if(client != null){
            url = fileStorageService.storeToS3(client.getNumeroClient().concat("/").concat("KYC"), type.toLowerCase(), file);
        } else {
            url = fileStorageService.storeToS3(mandataire.getCompteBancaire().getClient().getNumeroClient().concat("/").concat("Mandataires"), type.toLowerCase(), file);
        }
        PieceJustificative piece = new PieceJustificative();
        piece.setUrlFichier(url);
        piece.setLibelle(type);
        piece.setClient(client);
        piece.setMandataire(mandataire);
        piece.setExtension(file.getContentType());
        piece.setDateEnregistrement(LocalDateTime.now());
        piece = pieceJustificativeRepository.save(piece);
        return modelMapper.map(piece, PieceJustificativeDto.class);
    }

    @Override
    public List<PieceJustificativeDto> getByClient(UUID client) {
        return pieceJustificativeRepository.findByClientId(client).stream()
                .map(piece -> modelMapper.map(piece, PieceJustificativeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PieceJustificativeDto> getByMandataire(UUID mandataire) {
        return pieceJustificativeRepository.findByMandataireId(mandataire).stream()
                .map(piece -> modelMapper.map(piece, PieceJustificativeDto.class))
                .collect(Collectors.toList());
    }
}
