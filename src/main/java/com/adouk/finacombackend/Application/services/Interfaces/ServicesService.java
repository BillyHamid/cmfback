package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.FoirAQuestion;
import com.adouk.finacombackend.domain.aggregates.Services;

import java.util.List;
import java.util.UUID;

public interface ServicesService {
    Services create(Services faq);
    Services update(UUID id, Services faq);
    void delete(UUID id);
    List<Services> findAll();
}
