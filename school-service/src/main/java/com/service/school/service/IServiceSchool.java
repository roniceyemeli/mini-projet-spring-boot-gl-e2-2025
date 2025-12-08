package com.service.school.service;

import com.service.school.entity.School;
import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IServiceSchool {

    // ==================== CRUD OPERATIONS ====================

    School createSchool(School school);
    School updateSchool(Long id, School school);
    void deleteSchool(Long id);
    School getSchoolById(Long id);
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

    School changeSchoolStatus(Long schoolId, SchoolStatus status);
    School activateSchool(Long schoolId);
    School deactivateSchool(Long schoolId);
    School suspendSchool(Long schoolId);
    School verifySchool(Long schoolId);

    // ==================== FEATURED SCHOOLS ====================

    School setFeaturedStatus(Long schoolId, boolean isFeatured);
    List<School> getTopFeaturedSchools(int limit);

    // ==================== STATISTICS & COUNTERS ====================

    School incrementStudentCount(Long schoolId);
    School decrementStudentCount(Long schoolId);
    School incrementTeacherCount(Long schoolId);
    School decrementTeacherCount(Long schoolId);
    School updateStudentCount(Long schoolId, Integer count);
    School updateTeacherCount(Long schoolId, Integer count);

    // ==================== VALIDATION & CHECKS ====================

    boolean schoolExists(Long schoolId);
    boolean slugExists(String slug);
    boolean emailExists(String email);
    boolean isSchoolOperational(Long schoolId);
    boolean isSchoolAccredited(Long schoolId);
    boolean canSchoolAdmitStudents(Long schoolId);

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