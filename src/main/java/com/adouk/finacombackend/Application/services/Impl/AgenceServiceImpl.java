package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.services.Interfaces.AgenceService;
import com.adouk.finacombackend.Application.services.Interfaces.UserSessionQueryService;
import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.commands.AgenceCommand;
import com.adouk.finacombackend.infrastructure.repositories.AgenceRepository;
import com.adouk.finacombackend.infrastructure.repositories.AgentRepository;
import com.adouk.finacombackend.infrastructure.repositories.UserRepository;
import com.adouk.finacombackend.infrastructure.repositories.UserSessionRepository;
import com.adouk.finacombackend.infrastructure.rest.dto.UserDto;
import com.adouk.finacombackend.infrastructure.rest.dto.UserSessionDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AgenceServiceImpl implements AgenceService {

    private final AgenceRepository agenceRepository;
    private final ModelMapper modelMapper;
    private final AgentRepository agentRepository;

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence create(AgenceCommand agenceCommand) {
        Agence agence = modelMapper.map(agenceCommand, Agence.class);
        agence.setGestionnaire(agentRepository.findById(agenceCommand.getGestionnaireId()).orElseThrow());
        agence.setCreatedAt(new Date());
        return agenceRepository.save(agence);
    }

    @Override
    public Agence update(UUID id, AgenceCommand agence) {
       agenceRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Agence not found")
        );
        Agence agenceToUpdate = modelMapper.map(agence, Agence.class);
        agenceToUpdate.setUuid(id);
        agenceToUpdate.setGestionnaire(agentRepository.findById(agence.getGestionnaireId()).orElseThrow());
        agenceToUpdate.setUpdatedAt(new Date());
        agenceToUpdate.setActive(true);
        return agenceRepository.save(agenceToUpdate);
    }

    @Override
    public Agence toggleStatus(UUID id) {
        Agence agence = agenceRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Agence not found")
        );
        agence.setActive(!agence.isActive());
        agence.setUpdatedAt(new Date());
        return agenceRepository.save(agence);
    }

    @Override
    public void delete(UUID id) {
        agenceRepository.deleteById(id);
    }
}
