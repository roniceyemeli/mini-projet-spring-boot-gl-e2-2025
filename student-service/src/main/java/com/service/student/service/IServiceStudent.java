package com.service.student.service;

import com.service.student.dto.request.AcademicUpdateDTO;
import com.service.student.dto.request.CreateStudentDTO;
import com.service.student.dto.request.UpdateStudentDTO;
import com.service.student.dto.response.StudentDTO;
import com.service.student.dto.response.StudentResponseDTO;
import com.service.student.enums.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IServiceStudent {

    // ==================== CRUD OPERATIONS ====================

    StudentDTO createStudent(CreateStudentDTO studentDTO);
    StudentDTO updateStudent(UUID id, UpdateStudentDTO studentDTO);
    void deleteStudent(UUID id);
    StudentDTO getStudentById(UUID id);
    StudentResponseDTO getStudentResponseById(UUID id);
    StudentDTO getStudentByUserId(UUID userId);
    List<StudentResponseDTO> getAllStudents();

    // ==================== SEARCH & FILTER OPERATIONS ====================

    List<StudentResponseDTO> searchStudents(String keyword);
    List<StudentResponseDTO> getStudentsBySchool(UUID schoolId);
    List<StudentResponseDTO> getStudentsByProgram(String program);
    List<StudentResponseDTO> getStudentsByMajor(String major);
    List<StudentResponseDTO> getStudentsByEnrollmentStatus(EnrollmentStatus status);
    List<StudentResponseDTO> getStudentsByAcademicLevel(String academicLevel);
    List<StudentResponseDTO> getActiveStudents();
    List<StudentResponseDTO> getGraduatedStudents();
    List<StudentResponseDTO> getInternationalStudents();
    List<StudentResponseDTO> getStudentsByCommunity(UUID communityId);
    List<StudentResponseDTO> getStudentsByAdvisor(UUID advisorId);

    // ==================== STUDENT MANAGEMENT ====================

    StudentDTO changeEnrollmentStatus(UUID studentId, EnrollmentStatus status);
    StudentDTO activateStudent(UUID studentId);
    StudentDTO deactivateStudent(UUID studentId);
    StudentDTO suspendStudent(UUID studentId);
    StudentDTO graduateStudent(UUID studentId);

    // ==================== ACADEMIC MANAGEMENT ====================

    StudentDTO updateAcademicInfo(UUID studentId, AcademicUpdateDTO academicUpdate);
    StudentDTO addCredits(UUID studentId, Integer credits);
    StudentDTO updateGPA(UUID studentId, BigDecimal gpa);
    StudentDTO updateProgram(UUID studentId, String program, String major);

    // ==================== COMMUNICATION & SYNC ====================

    StudentResponseDTO getStudentWithUserInfo(UUID studentId);
    StudentDTO syncWithUserService(UUID studentId);
    StudentDTO syncWithAdvisorService(UUID studentId);
    boolean validateStudentUser(UUID studentId, UUID userId);

    // ==================== VALIDATION & UTILITY ====================

    boolean studentExists(UUID studentId);
    boolean userHasStudentProfile(UUID userId);
    boolean studentCodeExists(String studentCode);
    boolean isStudentActive(UUID studentId);

    // ==================== STATISTICS & ANALYTICS ====================

    long getTotalStudentsCount();
    long getActiveStudentsCount();
    long getGraduatedStudentsCount();
    long getInternationalStudentsCount();
    List<Object[]> getStudentsCountBySchool();
    List<Object[]> getStudentsCountByProgram();
    List<Object[]> getStudentsCountByEnrollmentStatus();
    List<Object[]> getStudentsCountByAcademicLevel();
    BigDecimal getAverageGPA();
    Integer getTotalCreditsAcrossStudents();

    // ==================== BULK OPERATIONS ====================

    void graduateEligibleStudents();
    void updateAcademicLevels();
    void deactivateInactiveStudents(LocalDate cutoffDate);
}
