package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServicesRepository extends JpaRepository<Services, UUID> {}
