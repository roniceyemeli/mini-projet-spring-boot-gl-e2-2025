package com.service.student.controller;

import com.service.student.dto.request.AcademicUpdateDTO;
import com.service.student.dto.request.CreateStudentDTO;
import com.service.student.dto.request.UpdateStudentDTO;
import com.service.student.dto.response.StudentDTO;
import com.service.student.dto.response.StudentResponseDTO;
import com.service.student.enums.EnrollmentStatus;
import com.service.student.service.IServiceStudent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentRestController {

    private final IServiceStudent studentService;

    /* =======================
       CREATE & READ
       ======================= */

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(
            @Valid @RequestBody CreateStudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(studentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentResponseById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentDTO> getStudentByUserId(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(
            @RequestParam(required = false) UUID schoolId,
            @RequestParam(required = false) String program,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) EnrollmentStatus status,
            @RequestParam(required = false) String academicLevel,
            @RequestParam(required = false) Boolean international,
            @RequestParam(required = false) Boolean graduated,
            @RequestParam(required = false) UUID communityId,
            @RequestParam(required = false) UUID advisorId) {

        if (schoolId != null)
            return ResponseEntity.ok(studentService.getStudentsBySchool(schoolId));

        if (program != null)
            return ResponseEntity.ok(studentService.getStudentsByProgram(program));

        if (major != null)
            return ResponseEntity.ok(studentService.getStudentsByMajor(major));

        if (status != null)
            return ResponseEntity.ok(studentService.getStudentsByEnrollmentStatus(status));

        if (academicLevel != null)
            return ResponseEntity.ok(studentService.getStudentsByAcademicLevel(academicLevel));

        if (Boolean.TRUE.equals(international))
            return ResponseEntity.ok(studentService.getInternationalStudents());

        if (Boolean.TRUE.equals(graduated))
            return ResponseEntity.ok(studentService.getGraduatedStudents());

        if (communityId != null)
            return ResponseEntity.ok(studentService.getStudentsByCommunity(communityId));

        if (advisorId != null)
            return ResponseEntity.ok(studentService.getStudentsByAdvisor(advisorId));

        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/active")
    public ResponseEntity<List<StudentResponseDTO>> getActiveStudents() {
        return ResponseEntity.ok(studentService.getActiveStudents());
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam String keyword) {
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    /* =======================
       UPDATE & DELETE
       ======================= */

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    /* =======================
       STATUS & ACADEMICS
       ======================= */

    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentDTO> changeEnrollmentStatus(
            @PathVariable UUID id,
            @RequestParam EnrollmentStatus status) {
        return ResponseEntity.ok(studentService.changeEnrollmentStatus(id, status));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StudentDTO> activateStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.activateStudent(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<StudentDTO> deactivateStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.deactivateStudent(id));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<StudentDTO> suspendStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.suspendStudent(id));
    }

    @PatchMapping("/{id}/graduate")
    public ResponseEntity<StudentDTO> graduateStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.graduateStudent(id));
    }

    @PatchMapping("/{id}/academic")
    public ResponseEntity<StudentDTO> updateAcademicInfo(
            @PathVariable UUID id,
            @RequestBody AcademicUpdateDTO academicUpdate) {
        return ResponseEntity.ok(studentService.updateAcademicInfo(id, academicUpdate));
    }

    @PatchMapping("/{id}/add-credits")
    public ResponseEntity<StudentDTO> addCredits(
            @PathVariable UUID id,
            @RequestParam Integer credits) {
        return ResponseEntity.ok(studentService.addCredits(id, credits));
    }

    /* =======================
       SYNC & VALIDATION
       ======================= */

    @GetMapping("/{id}/with-user")
    public ResponseEntity<StudentResponseDTO> getStudentWithUserInfo(
            @PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentWithUserInfo(id));
    }

    @GetMapping("/{id}/sync-user")
    public ResponseEntity<StudentDTO> syncWithUserService(
            @PathVariable UUID id) {
        return ResponseEntity.ok(studentService.syncWithUserService(id));
    }

    @GetMapping("/{id}/validate-user/{userId}")
    public ResponseEntity<Boolean> validateStudentUser(
            @PathVariable UUID id,
            @PathVariable UUID userId) {
        return ResponseEntity.ok(studentService.validateStudentUser(id, userId));
    }

    /* =======================
       STATISTICS
       ======================= */

    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalStudentsCount() {
        return ResponseEntity.ok(studentService.getTotalStudentsCount());
    }

    @GetMapping("/stats/active")
    public ResponseEntity<Long> getActiveStudentsCount() {
        return ResponseEntity.ok(studentService.getActiveStudentsCount());
    }

    @GetMapping("/stats/graduated")
    public ResponseEntity<Long> getGraduatedStudentsCount() {
        return ResponseEntity.ok(studentService.getGraduatedStudentsCount());
    }

    @GetMapping("/stats/international")
    public ResponseEntity<Long> getInternationalStudentsCount() {
        return ResponseEntity.ok(studentService.getInternationalStudentsCount());
    }

    @GetMapping("/stats/gpa-average")
    public ResponseEntity<BigDecimal> getAverageGPA() {
        return ResponseEntity.ok(studentService.getAverageGPA());
    }

    @GetMapping("/stats/total-credits")
    public ResponseEntity<Integer> getTotalCreditsAcrossStudents() {
        return ResponseEntity.ok(studentService.getTotalCreditsAcrossStudents());
    }

    @GetMapping("/stats/by-school")
    public ResponseEntity<List<Object[]>> getStudentsCountBySchool() {
        return ResponseEntity.ok(studentService.getStudentsCountBySchool());
    }

    @GetMapping("/stats/by-program")
    public ResponseEntity<List<Object[]>> getStudentsCountByProgram() {
        return ResponseEntity.ok(studentService.getStudentsCountByProgram());
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStudentsCountByEnrollmentStatus() {
        return ResponseEntity.ok(studentService.getStudentsCountByEnrollmentStatus());
    }

    @GetMapping("/stats/by-level")
    public ResponseEntity<List<Object[]>> getStudentsCountByAcademicLevel() {
        return ResponseEntity.ok(studentService.getStudentsCountByAcademicLevel());
    }

    /* =======================
       BULK OPERATIONS
       ======================= */

    @PostMapping("/batch/graduate-eligible")
    public ResponseEntity<Void> graduateEligibleStudents() {
        studentService.graduateEligibleStudents();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch/update-levels")
    public ResponseEntity<Void> updateAcademicLevels() {
        studentService.updateAcademicLevels();
        return ResponseEntity.ok().build();
    }
}
