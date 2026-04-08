package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.FoirAQuestionService;
import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.FoirAQuestion;
import com.adouk.finacombackend.infrastructure.repositories.FoirAQuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FoirAQuestionServiceImpl implements FoirAQuestionService {

    private final FoirAQuestionRepository repository;
    private final AuthenticationService userService;

    @Override
    public FoirAQuestion create(FoirAQuestion faq) {
        faq.setCreatedAt(new Date());
        faq.setCreatedBy(userService.getConectedUser().getAgent().getFirstname() + " " + userService.getConectedUser().getAgent().getLastname());
        return repository.save(faq);
    }

    @Override
    public FoirAQuestion update(UUID id, FoirAQuestion faq) {
        FoirAQuestion old = repository.findById(id).orElseThrow();
        faq.setUpdatedAt(new Date());
        faq.setUpdatedBy(userService.getConectedUser().getAgent().getFirstname() + " " + userService.getConectedUser().getAgent().getLastname());
        faq.setCreatedAt(old.getCreatedAt());
        faq.setCreatedBy(old.getCreatedBy());
        faq.setId(id);
        return repository.save(faq);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<FoirAQuestion> findAll() {
        return repository.findAll();
    }
}
