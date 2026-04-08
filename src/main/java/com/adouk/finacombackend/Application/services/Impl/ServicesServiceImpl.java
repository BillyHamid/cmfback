package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.Application.configs.AuthenticationService;
import com.adouk.finacombackend.Application.services.Interfaces.ServicesService;
import com.adouk.finacombackend.domain.aggregates.Services;
import com.adouk.finacombackend.infrastructure.repositories.ServicesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository repository;
    private final AuthenticationService userService;

    @Override
    public Services create(Services faq) {
        faq.setCreatedAt(new Date());
        faq.setCreatedBy(userService.getConectedUser().getAgent().getFirstname() + " " + userService.getConectedUser().getAgent().getLastname());
        return repository.save(faq);
    }

    @Override
    public Services update(UUID id, Services faq) {
        Services old = repository.findById(id).orElseThrow();
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
    public List<Services> findAll() {
        return repository.findAll();
    }
}
