package com.adouk.finacombackend.Application.services.Interfaces;

import com.adouk.finacombackend.domain.aggregates.FoirAQuestion;

import java.util.List;
import java.util.UUID;

public interface FoirAQuestionService {
    FoirAQuestion create(FoirAQuestion faq);
    FoirAQuestion update(UUID id, FoirAQuestion faq);
    void delete(UUID id);
    List<FoirAQuestion> findAll();
}
