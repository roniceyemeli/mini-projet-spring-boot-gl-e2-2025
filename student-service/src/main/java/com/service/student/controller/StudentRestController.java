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

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentRestController {

    private final IServiceStudent studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody CreateStudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(studentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentResponseById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentDTO> getStudentByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) String program,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) EnrollmentStatus status,
            @RequestParam(required = false) String academicLevel,
            @RequestParam(required = false) Boolean international,
            @RequestParam(required = false) Boolean graduated,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Long advisorId) {

        List<StudentResponseDTO> students;

        if (schoolId != null) {
            students = studentService.getStudentsBySchool(schoolId);
        } else if (program != null) {
            students = studentService.getStudentsByProgram(program);
        } else if (major != null) {
            students = studentService.getStudentsByMajor(major);
        } else if (status != null) {
            students = studentService.getStudentsByEnrollmentStatus(status);
        } else if (academicLevel != null) {
            students = studentService.getStudentsByAcademicLevel(academicLevel);
        } else if (international != null && international) {
            students = studentService.getInternationalStudents();
        } else if (graduated != null && graduated) {
            students = studentService.getGraduatedStudents();
        } else if (communityId != null) {
            students = studentService.getStudentsByCommunity(communityId);
        } else if (advisorId != null) {
            students = studentService.getStudentsByAdvisor(advisorId);
        } else {
            students = studentService.getAllStudents();
        }

        return ResponseEntity.ok(students);
    }

    @GetMapping("/active")
    public ResponseEntity<List<StudentResponseDTO>> getActiveStudents() {
        return ResponseEntity.ok(studentService.getActiveStudents());
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(@RequestParam String keyword) {
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/with-user")
    public ResponseEntity<StudentResponseDTO> getStudentWithUserInfo(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentWithUserInfo(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentDTO> changeEnrollmentStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        return ResponseEntity.ok(studentService.changeEnrollmentStatus(id, status));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StudentDTO> activateStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.activateStudent(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<StudentDTO> deactivateStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deactivateStudent(id));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<StudentDTO> suspendStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.suspendStudent(id));
    }

    @PatchMapping("/{id}/graduate")
    public ResponseEntity<StudentDTO> graduateStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.graduateStudent(id));
    }

    @PatchMapping("/{id}/academic")
    public ResponseEntity<StudentDTO> updateAcademicInfo(
            @PathVariable Long id,
            @RequestBody AcademicUpdateDTO academicUpdate) {
        return ResponseEntity.ok(studentService.updateAcademicInfo(id, academicUpdate));
    }

    @PatchMapping("/{id}/add-credits")
    public ResponseEntity<StudentDTO> addCredits(
            @PathVariable Long id,
            @RequestParam Integer credits) {
        return ResponseEntity.ok(studentService.addCredits(id, credits));
    }

    @GetMapping("/{id}/sync-user")
    public ResponseEntity<StudentDTO> syncWithUserService(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.syncWithUserService(id));
    }

    @GetMapping("/{id}/validate-user/{userId}")
    public ResponseEntity<Boolean> validateStudentUser(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(studentService.validateStudentUser(id, userId));
    }

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
    public ResponseEntity<Double> getAverageGPA() {
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