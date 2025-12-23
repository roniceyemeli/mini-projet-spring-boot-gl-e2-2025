package com.service.user.repository;

import com.service.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

    List<Role> findByIsDefaultTrue();

    List<Role> findByIsSystemFalse();

    @Query("SELECT r FROM Role r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Role> searchRoles(@Param("keyword") String keyword);

    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE r.isSystem = false ORDER BY r.name ASC")
    List<Role> findAllCustomRoles();
}