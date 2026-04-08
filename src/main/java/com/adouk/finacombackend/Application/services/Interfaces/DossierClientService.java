package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.DossierClient;
import com.adouk.finacombackend.domain.commands.AuthCommand;
import com.adouk.finacombackend.domain.commands.ValidationDossierCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.AuthResponseDto;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface DossierClientService {

    List<DossierClient> getPending();
    List<DossierClient> getTraiter();
    DossierClient getById(UUID id);
    DossierClient validateKyc(ValidationDossierCommand command);
    DossierClient rejectedKyc(ValidationDossierCommand command);
}
