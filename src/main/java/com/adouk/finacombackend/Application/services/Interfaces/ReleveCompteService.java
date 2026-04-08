package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.ReleveCompte;
import com.adouk.finacombackend.domain.commands.ReleveCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.ReleveCompteDto;
import com.adouk.finacombackend.infrastructure.rest.dto.ReleveCompteSearch;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ReleveCompteService {

    List<ReleveCompte> findAll();
    ReleveCompte create(ReleveCommand command);
    ReleveCompte submitReleve(UUID releveId, MultipartFile file);
    ReleveCompte rejectReleve(UUID releveId, String reason);
    List<ReleveCompteDto> findByCompteBancaireId(UUID id);
    Page<ReleveCompte> findAllFiltered(ReleveCompteSearch search);

}
