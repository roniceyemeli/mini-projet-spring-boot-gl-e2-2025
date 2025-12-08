package com.service.school.repository;

import com.service.school.entity.School;
import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findBySlug(String slug);

    Optional<School> findByEmail(String email);

    List<School> findByType(SchoolType type);

    List<School> findByStatus(SchoolStatus status);

    List<School> findByCountry(String country);

    List<School> findByCity(String city);

    List<School> findByIsFeaturedTrueAndStatus(SchoolStatus status);

    List<School> findByVerifiedTrueAndStatus(SchoolStatus status);

    List<School> findByStatusAndIsActiveTrue(SchoolStatus status);

    List<School> findByIsPublicTrueAndStatus(SchoolStatus status);

    List<School> findByIsPublicFalseAndStatus(SchoolStatus status);

    boolean existsBySlug(String slug);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM School s WHERE " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.country) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND s.status = 'ACTIVE' " +
            "ORDER BY s.isFeatured DESC, s.name ASC")
    List<School> searchSchools(@Param("keyword") String keyword);

    @Query("SELECT s FROM School s WHERE s.status = 'ACTIVE' AND s.isFeatured = true " +
            "ORDER BY s.ranking ASC NULLS LAST, s.totalStudents DESC " +
            "LIMIT :limit")
    List<School> findTopFeaturedSchools(@Param("limit") int limit);

    @Query("SELECT s.type, COUNT(s) FROM School s WHERE s.status = 'ACTIVE' GROUP BY s.type")
    List<Object[]> countSchoolsByType();

    @Query("SELECT s.status, COUNT(s) FROM School s GROUP BY s.status")
    List<Object[]> countSchoolsByStatus();

    @Query("SELECT s.country, COUNT(s) FROM School s WHERE s.status = 'ACTIVE' GROUP BY s.country")
    List<Object[]> countSchoolsByCountry();

    @Query("SELECT COALESCE(SUM(s.totalStudents), 0) FROM School s WHERE s.status = 'ACTIVE'")
    Integer sumTotalStudents();

    @Query("SELECT COALESCE(SUM(s.totalTeachers), 0) FROM School s WHERE s.status = 'ACTIVE'")
    Integer sumTotalTeachers();

    Long countByStatusAndIsActiveTrue(SchoolStatus status);

    Long countByIsFeaturedTrueAndStatus(SchoolStatus status);

    Long countByVerifiedTrueAndStatus(SchoolStatus status);

    // Simplified location query - in production use PostGIS extension
    @Query("SELECT s FROM School s WHERE " +
            "s.latitude IS NOT NULL AND s.longitude IS NOT NULL AND " +
            "s.status = 'ACTIVE' AND " +
            "SQRT(POWER((s.latitude - :latitude), 2) + POWER((s.longitude - :longitude), 2)) * 111.32 <= :radiusKm")
    List<School> findSchoolsNearLocation(@Param("latitude") Double latitude,
                                         @Param("longitude") Double longitude,
                                         @Param("radiusKm") Double radiusKm);

    @Query("SELECT s FROM School s WHERE s.updatedAt < :cutoffDate AND s.isActive = true")
    List<School> findInactiveSchoolsSince(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Region can be derived from city/country or stored as separate field
    @Query("SELECT s FROM School s WHERE " +
            "(LOWER(s.city) LIKE LOWER(CONCAT('%', :region, '%')) OR " +
            "LOWER(s.country) LIKE LOWER(CONCAT('%', :region, '%'))) " +
            "AND s.status = 'ACTIVE'")
    List<School> findByRegion(@Param("region") String region);
}