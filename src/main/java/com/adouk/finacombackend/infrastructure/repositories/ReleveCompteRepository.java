package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.domain.aggregates.ReleveCompte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReleveCompteRepository extends PagingAndSortingRepository<ReleveCompte, UUID> {

    ReleveCompte save(ReleveCompte releveCompte);
    Optional<ReleveCompte> findById(UUID id);
    List<ReleveCompte> findAll();
    List<ReleveCompte> findByCompteBancaireId(UUID id);
    Page<ReleveCompte> findByStatut(String statut, Pageable pageable);

    @Query("SELECT r FROM ReleveCompte r WHERE r.compteBancaire.numeroCompte = :compte")
    Page<ReleveCompte> findByCompte(@Param("compte") String compte, Pageable pageable);

    @Query("SELECT r FROM ReleveCompte r WHERE r.statut = :statut AND r.compteBancaire.numeroCompte = :compte")
    Page<ReleveCompte> findByStatutAndCompte(
            @Param("statut") String statut,
            @Param("compte") String compte,
            Pageable pageable);


}
