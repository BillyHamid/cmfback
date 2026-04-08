package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.commands.CompteBancaireCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.CompteBancaireDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CompteBancaireService {
    CompteBancaireDto creerCompte(CompteBancaireCommand command, Map<String, MultipartFile> fichiers);
    CompteBancaireDto findById(UUID id);
    List<CompteBancaireDto> getClientComptes(UUID clientId);
    List<CompteBancaireDto> getCurrentClientComptes();

}

