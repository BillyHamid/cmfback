package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.role.code != 'client'")
    List<User> findAllAgents();

    @Query("select u from User u where u.client.id = :clientId")
    Optional<User> findByClientId(UUID clientId);

}
