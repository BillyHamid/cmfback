package com.adouk.finacombackend.infrastructure.repositories;

import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    @Query("select rp.permission from RolePermission rp where rp.role.id = ?1")
    List<Permission> findPermissionByRole(UUID role);

    @Query("select rp from RolePermission rp where rp.role.id = ?1")
    List<RolePermission> findByRole(UUID role);

}

