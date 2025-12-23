package com.service.student.repository;

import com.service.student.entity.Student;
import com.service.student.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    /* =======================
       Basic finders
       ======================= */

    Optional<Student> findByUserId(UUID userId);

    Optional<Student> findByStudentCode(String studentCode);

    List<Student> findBySchoolId(UUID schoolId);

    List<Student> findByProgram(String program);

    List<Student> findByMajor(String major);

    List<Student> findByEnrollmentStatus(EnrollmentStatus status);

    List<Student> findByAcademicLevel(String academicLevel);

    List<Student> findByIsActiveTrue();

    List<Student> findByIsGraduatedTrue();

    List<Student> findByIsInternationalTrue();

    List<Student> findByCommunityId(UUID communityId);

    List<Student> findByClubId(UUID clubId);

    List<Student> findByAdvisorId(UUID advisorId);

    /* =======================
       Exists & counts
       ======================= */

    boolean existsByUserId(UUID userId);

    boolean existsByStudentCode(String studentCode);

    long countByEnrollmentStatus(EnrollmentStatus status);

    long countByIsInternationalTrue();

    /* =======================
       Search
       ======================= */

    @Query("""
        SELECT s FROM Student s
        WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.program) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.major) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Student> searchStudents(@Param("keyword") String keyword);

    /* =======================
       Analytics / reporting
       ======================= */

    @Query("""
        SELECT s.schoolId, COUNT(s)
        FROM Student s
        WHERE s.enrollmentStatus = 'ACTIVE'
        GROUP BY s.schoolId
    """)
    List<Object[]> countStudentsBySchool();

    @Query("""
        SELECT s.program, COUNT(s)
        FROM Student s
        WHERE s.enrollmentStatus = 'ACTIVE'
        GROUP BY s.program
    """)
    List<Object[]> countStudentsByProgram();

    @Query("""
        SELECT s.enrollmentStatus, COUNT(s)
        FROM Student s
        GROUP BY s.enrollmentStatus
    """)
    List<Object[]> countStudentsByEnrollmentStatus();

    @Query("""
        SELECT s.academicLevel, COUNT(s)
        FROM Student s
        WHERE s.enrollmentStatus = 'ACTIVE'
        GROUP BY s.academicLevel
    """)
    List<Object[]> countStudentsByAcademicLevel();

    @Query("""
        SELECT AVG(s.gpa)
        FROM Student s
        WHERE s.gpa IS NOT NULL
          AND s.enrollmentStatus = 'ACTIVE'
    """)
    Double averageGPA();

    @Query("""
        SELECT SUM(s.completedCredits)
        FROM Student s
        WHERE s.enrollmentStatus = 'ACTIVE'
    """)
    Integer sumCompletedCredits();

    /* =======================
       Business rules
       ======================= */

    @Query("""
        SELECT s FROM Student s
        WHERE s.enrollmentStatus = 'ACTIVE'
          AND s.expectedGraduationYear <= YEAR(CURRENT_DATE)
          AND s.completedCredits >= s.totalCredits
          AND s.isGraduated = false
    """)
    List<Student> findEligibleForGraduation();

    @Query("""
        SELECT s FROM Student s
        WHERE s.updatedAt < :cutoffDate
          AND s.isActive = true
    """)
    List<Student> findInactiveStudentsSince(@Param("cutoffDate") LocalDate cutoffDate);
}
