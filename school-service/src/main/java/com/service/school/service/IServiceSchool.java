package com.service.school.service;

import com.service.school.entity.School;
import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServiceSchool {

    // ==================== CRUD OPERATIONS ====================

    School createSchool(School school);
    School updateSchool(UUID id, School school);
    void deleteSchool(UUID id);
    School getSchoolById(UUID id);
    Optional<School> getSchoolBySlug(String slug);
    Optional<School> getSchoolByEmail(String email);
    List<School> getAllSchools();

    // ==================== SEARCH & FILTER OPERATIONS ====================

    List<School> searchSchools(String keyword);
    List<School> getSchoolsByType(SchoolType type);
    List<School> getSchoolsByStatus(SchoolStatus status);
    List<School> getSchoolsByCountry(String country);
    List<School> getSchoolsByCity(String city);
    List<School> getFeaturedSchools();
    List<School> getVerifiedSchools();
    List<School> getActiveSchools();
    List<School> getPublicSchools();
    List<School> getPrivateSchools();

    // ==================== STATUS MANAGEMENT ====================

    School changeSchoolStatus(UUID schoolId, SchoolStatus status);
    School activateSchool(UUID schoolId);
    School deactivateSchool(UUID schoolId);
    School suspendSchool(UUID schoolId);
    School verifySchool(UUID schoolId);

    // ==================== FEATURED SCHOOLS ====================

    School setFeaturedStatus(UUID schoolId, boolean isFeatured);
    List<School> getTopFeaturedSchools(int limit);

    // ==================== STATISTICS & COUNTERS ====================

    School incrementStudentCount(UUID schoolId);
    School decrementStudentCount(UUID schoolId);
    School incrementTeacherCount(UUID schoolId);
    School decrementTeacherCount(UUID schoolId);
    School updateStudentCount(UUID schoolId, Integer count);
    School updateTeacherCount(UUID schoolId, Integer count);

    // ==================== VALIDATION & CHECKS ====================

    boolean schoolExists(UUID schoolId);
    boolean slugExists(String slug);
    boolean emailExists(String email);
    boolean isSchoolOperational(UUID schoolId);
    boolean isSchoolAccredited(UUID schoolId);
    boolean canSchoolAdmitStudents(UUID schoolId);

    // ==================== STATISTICS & ANALYTICS ====================

    Long getTotalSchoolsCount();
    Long getActiveSchoolsCount();
    Long getFeaturedSchoolsCount();
    Long getVerifiedSchoolsCount();
    List<Object[]> getSchoolsCountByType();
    List<Object[]> getSchoolsCountByStatus();
    List<Object[]> getSchoolsCountByCountry();
    Integer getTotalStudentsAcrossSchools();
    Integer getTotalTeachersAcrossSchools();

    // ==================== LOCATION BASED QUERIES ====================

    List<School> getSchoolsNearLocation(Double latitude, Double longitude, Double radiusKm);
    List<School> getSchoolsInRegion(String region);

    // ==================== BULK OPERATIONS ====================

    void verifyAllPendingSchools();
    void deactivateInactiveSchools(LocalDateTime cutoffDate);
    void updateAccreditationStatuses();
}