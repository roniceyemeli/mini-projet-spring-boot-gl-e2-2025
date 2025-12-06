package com.service.community.repository;

import com.service.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

    Optional<Community> findBySlug(String slug);

    Optional<Community> findByTitle(String title);

    List<Community> findByIsActiveTrue();

    List<Community> findByIsActive(Boolean isActive);

    List<Community> findByMemberCountGreaterThan(Integer minMembers);

    @Query("SELECT c FROM Community c WHERE " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.slug) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Community> searchCommunities(@Param("keyword") String keyword);

    @Query("SELECT c FROM Community c ORDER BY c.memberCount DESC")
    List<Community> findAllOrderByMemberCountDesc();

    boolean existsBySlug(String slug);

    boolean existsByTitle(String title);
}