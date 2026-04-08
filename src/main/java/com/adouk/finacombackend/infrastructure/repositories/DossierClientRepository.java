package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.DossierClient;
import com.adouk.finacombackend.domain.aggregates.StatusDossierClient;
import com.adouk.finacombackend.domain.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DossierClientRepository extends JpaRepository<DossierClient, UUID> {
    List<DossierClient> findByStatutDossier(StatusDossierClient statutDossier);

    @Query("SELECT d FROM DossierClient d WHERE d.prospectClient.telephone1 = :telephone1 and d.statutDossier = :statutDossier")
    Optional<DossierClient> findByTelephone1AndStatutDossier(String telephone1, StatusDossierClient statutDossier);

    @Query("SELECT d FROM DossierClient d WHERE d.statutDossier != :statutDossier")
    List<DossierClient> findByStatutDossierDiff(StatusDossierClient statutDossier);

    List<DossierClient> findByAgentCommercialOrderByDateSoumissionDesc(User agentCommercial);

    long countByAgentCommercialAndStatutDossier(User agentCommercial, StatusDossierClient statutDossier);
}
