package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.commands.AgenceCommand;
import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;

import java.util.List;
import java.util.UUID;

public interface AgenceService {

    List<Agence> findAll();
    Agence create(AgenceCommand agence);
    Agence update(UUID id, AgenceCommand agence);
    Agence toggleStatus(UUID id);
    void delete(UUID id);


}
