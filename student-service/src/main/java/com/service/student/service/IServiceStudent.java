package com.service.student.service;

import com.service.student.dto.request.AcademicUpdateDTO;
import com.service.student.dto.request.CreateStudentDTO;
import com.service.student.dto.request.UpdateStudentDTO;
import com.service.student.dto.response.StudentDTO;
import com.service.student.dto.response.StudentResponseDTO;
import com.service.student.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface IServiceStudent {

    // ==================== CRUD OPERATIONS ====================

    StudentDTO createStudent(CreateStudentDTO studentDTO);
    StudentDTO updateStudent(Long id, UpdateStudentDTO studentDTO);
    void deleteStudent(Long id);
    StudentDTO getStudentById(Long id);
    StudentResponseDTO getStudentResponseById(Long id);
    StudentDTO getStudentByUserId(Long userId);
    List<StudentResponseDTO> getAllStudents();

    // ==================== SEARCH & FILTER OPERATIONS ====================

    List<StudentResponseDTO> searchStudents(String keyword);
    List<StudentResponseDTO> getStudentsBySchool(Long schoolId);
    List<StudentResponseDTO> getStudentsByProgram(String program);
    List<StudentResponseDTO> getStudentsByMajor(String major);
    List<StudentResponseDTO> getStudentsByEnrollmentStatus(EnrollmentStatus status);
    List<StudentResponseDTO> getStudentsByAcademicLevel(String academicLevel);
    List<StudentResponseDTO> getActiveStudents();
    List<StudentResponseDTO> getGraduatedStudents();
    List<StudentResponseDTO> getInternationalStudents();
    List<StudentResponseDTO> getStudentsByCommunity(Long communityId);
    List<StudentResponseDTO> getStudentsByAdvisor(Long advisorId);

    // ==================== STUDENT MANAGEMENT ====================

    StudentDTO changeEnrollmentStatus(Long studentId, EnrollmentStatus status);
    StudentDTO activateStudent(Long studentId);
    StudentDTO deactivateStudent(Long studentId);
    StudentDTO suspendStudent(Long studentId);
    StudentDTO graduateStudent(Long studentId);

    // ==================== ACADEMIC MANAGEMENT ====================

    StudentDTO updateAcademicInfo(Long studentId, AcademicUpdateDTO academicUpdate);
    StudentDTO addCredits(Long studentId, Integer credits);
    StudentDTO updateGPA(Long studentId, Double gpa);
    StudentDTO updateProgram(Long studentId, String program, String major);

    // ==================== COMMUNICATION & SYNC ====================

    StudentResponseDTO getStudentWithUserInfo(Long studentId);
    StudentDTO syncWithUserService(Long studentId);
    boolean validateStudentUser(Long studentId, Long userId);

    // ==================== VALIDATION & UTILITY ====================

    boolean studentExists(Long studentId);
    boolean userHasStudentProfile(Long userId);
    boolean studentCodeExists(String studentCode);
    boolean isStudentActive(Long studentId);

    // ==================== STATISTICS & ANALYTICS ====================

    Long getTotalStudentsCount();
    Long getActiveStudentsCount();
    Long getGraduatedStudentsCount();
    Long getInternationalStudentsCount();
    List<Object[]> getStudentsCountBySchool();
    List<Object[]> getStudentsCountByProgram();
    List<Object[]> getStudentsCountByEnrollmentStatus();
    List<Object[]> getStudentsCountByAcademicLevel();
    Double getAverageGPA();
    Integer getTotalCreditsAcrossStudents();

    // ==================== BULK OPERATIONS ====================

    void graduateEligibleStudents();
    void updateAcademicLevels();
    void deactivateInactiveStudents(LocalDate cutoffDate);
}