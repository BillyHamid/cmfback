package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.FoirAQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoirAQuestionRepository extends JpaRepository<FoirAQuestion, UUID> {

}
