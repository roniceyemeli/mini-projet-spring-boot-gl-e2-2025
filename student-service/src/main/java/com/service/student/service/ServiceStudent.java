package com.service.student.service;

import com.service.student.dto.student.*;
import com.service.student.entity.Student;
import com.service.student.enums.EnrollmentStatus;
import com.service.student.repository.StudentRepository;
import com.service.user.client.UserServiceClient;
import com.service.user.dto.user.UserMinimalDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceStudent implements IServiceStudent {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public StudentDTO createStudent(CreateStudentDTO studentDTO) {
        log.info("Creating new student for user ID: {}", studentDTO.getUserId());

        // Validate user doesn't already have a student profile
        if (studentRepository.existsByUserId(studentDTO.getUserId())) {
            throw new RuntimeException("User already has a student profile");
        }

        // Validate student code uniqueness
        if (studentDTO.getStudentCode() != null &&
                studentRepository.existsByStudentCode(studentDTO.getStudentCode())) {
            throw new RuntimeException("Student code already exists");
        }

        // Map DTO to Entity
        Student student = modelMapper.map(studentDTO, Student.class);

        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", savedStudent.getId());

        return modelMapper.map(savedStudent, StudentDTO.class);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        log.debug("Fetching student by ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public StudentResponseDTO getStudentResponseById(Long id) {
        log.debug("Fetching student response by ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);

        // Fetch user info from user-service
        try {
            UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());
            response.setUserInfo(userInfo);
        } catch (Exception e) {
            log.warn("Could not fetch user info for student {}: {}", id, e.getMessage());
        }

        return response;
    }

    @Override
    public StudentDTO getStudentByUserId(Long userId) {
        log.debug("Fetching student by user ID: {}", userId);

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found for user id: " + userId));

        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        log.debug("Fetching all students");

        return studentRepository.findAll().stream()
                .map(student -> {
                    StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);

                    // Fetch user info for each student
                    try {
                        UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());
                        response.setUserInfo(userInfo);
                    } catch (Exception e) {
                        log.warn("Could not fetch user info for student {}: {}", student.getId(), e.getMessage());
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO updateStudent(Long id, UpdateStudentDTO studentDTO) {
        log.info("Updating student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Update fields if provided
        if (studentDTO.getFirstName() != null) {
            student.setFirstName(studentDTO.getFirstName());
        }
        if (studentDTO.getLastName() != null) {
            student.setLastName(studentDTO.getLastName());
        }
        if (studentDTO.getDateOfBirth() != null) {
            student.setDateOfBirth(studentDTO.getDateOfBirth());
        }
        if (studentDTO.getGender() != null) {
            student.setGender(studentDTO.getGender());
        }
        if (studentDTO.getNationality() != null) {
            student.setNationality(studentDTO.getNationality());
        }
        if (studentDTO.getCitizenship() != null) {
            student.setCitizenship(studentDTO.getCitizenship());
        }
        if (studentDTO.getIdCardNumber() != null) {
            student.setIdCardNumber(studentDTO.getIdCardNumber());
        }
        if (studentDTO.getPassportNumber() != null) {
            student.setPassportNumber(studentDTO.getPassportNumber());
        }
        if (studentDTO.getAddress() != null) {
            student.setAddress(studentDTO.getAddress());
        }
        if (studentDTO.getCity() != null) {
            student.setCity(studentDTO.getCity());
        }
        if (studentDTO.getState() != null) {
            student.setState(studentDTO.getState());
        }
        if (studentDTO.getCountry() != null) {
            student.setCountry(studentDTO.getCountry());
        }
        if (studentDTO.getPostalCode() != null) {
            student.setPostalCode(studentDTO.getPostalCode());
        }
        if (studentDTO.getPhoneNumber() != null) {
            student.setPhoneNumber(studentDTO.getPhoneNumber());
        }
        if (studentDTO.getEmergencyPhone() != null) {
            student.setEmergencyPhone(studentDTO.getEmergencyPhone());
        }
        if (studentDTO.getEmail() != null) {
            student.setEmail(studentDTO.getEmail());
        }
        if (studentDTO.getPersonalEmail() != null) {
            student.setPersonalEmail(studentDTO.getPersonalEmail());
        }
        if (studentDTO.getSchoolId() != null) {
            student.setSchoolId(studentDTO.getSchoolId());
        }
        if (studentDTO.getProgram() != null) {
            student.setProgram(studentDTO.getProgram());
        }
        if (studentDTO.getMajor() != null) {
            student.setMajor(studentDTO.getMajor());
        }
        if (studentDTO.getMinor() != null) {
            student.setMinor(studentDTO.getMinor());
        }
        if (studentDTO.getEnrollmentYear() != null) {
            student.setEnrollmentYear(studentDTO.getEnrollmentYear());
        }
        if (studentDTO.getExpectedGraduationYear() != null) {
            student.setExpectedGraduationYear(studentDTO.getExpectedGraduationYear());
        }
        if (studentDTO.getCommunityId() != null) {
            student.setCommunityId(studentDTO.getCommunityId());
        }
        if (studentDTO.getClubId() != null) {
            student.setClubId(studentDTO.getClubId());
        }
        if (studentDTO.getAdvisorId() != null) {
            student.setAdvisorId(studentDTO.getAdvisorId());
        }
        if (studentDTO.getProfilePicture() != null) {
            student.setProfilePicture(studentDTO.getProfilePicture());
        }
        if (studentDTO.getResumeUrl() != null) {
            student.setResumeUrl(studentDTO.getResumeUrl());
        }
        if (studentDTO.getLinkedinUrl() != null) {
            student.setLinkedinUrl(studentDTO.getLinkedinUrl());
        }
        if (studentDTO.getGithubUrl() != null) {
            student.setGithubUrl(studentDTO.getGithubUrl());
        }
        if (studentDTO.getPortfolioUrl() != null) {
            student.setPortfolioUrl(studentDTO.getPortfolioUrl());
        }
        if (studentDTO.getIsInternational() != null) {
            student.setIsInternational(studentDTO.getIsInternational());
        }
        if (studentDTO.getVisaStatus() != null) {
            student.setVisaStatus(studentDTO.getVisaStatus());
        }
        if (studentDTO.getFinancialAidStatus() != null) {
            student.setFinancialAidStatus(studentDTO.getFinancialAidStatus());
        }
        if (studentDTO.getScholarshipName() != null) {
            student.setScholarshipName(studentDTO.getScholarshipName());
        }
        if (studentDTO.getDisabilities() != null) {
            student.setDisabilities(studentDTO.getDisabilities());
        }
        if (studentDTO.getSpecialNeeds() != null) {
            student.setSpecialNeeds(studentDTO.getSpecialNeeds());
        }
        if (studentDTO.getMedicalConditions() != null) {
            student.setMedicalConditions(studentDTO.getMedicalConditions());
        }
        if (studentDTO.getEmergencyContactName() != null) {
            student.setEmergencyContactName(studentDTO.getEmergencyContactName());
        }
        if (studentDTO.getEmergencyContactRelationship() != null) {
            student.setEmergencyContactRelationship(studentDTO.getEmergencyContactRelationship());
        }
        if (studentDTO.getEmergencyContactAddress() != null) {
            student.setEmergencyContactAddress(studentDTO.getEmergencyContactAddress());
        }
        if (studentDTO.getEmergencyContactEmail() != null) {
            student.setEmergencyContactEmail(studentDTO.getEmergencyContactEmail());
        }
        if (studentDTO.getIsActive() != null) {
            student.setIsActive(studentDTO.getIsActive());
        }
        if (studentDTO.getIsGraduated() != null) {
            student.setIsGraduated(studentDTO.getIsGraduated());
        }
        if (studentDTO.getNotes() != null) {
            student.setNotes(studentDTO.getNotes());
        }
        if (studentDTO.getUpdatedBy() != null) {
            student.setUpdatedBy(studentDTO.getUpdatedBy());
        }

        Student updatedStudent = studentRepository.save(student);
        log.info("Student updated successfully: {}", updatedStudent.getFullName());

        return modelMapper.map(updatedStudent, StudentDTO.class);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Soft delete (deactivate)
        student.setIsActive(false);
        student.setEnrollmentStatus(EnrollmentStatus.WITHDRAWN);
        studentRepository.save(student);

        log.info("Student deactivated: {}", student.getFullName());
    }

    @Override
    public List<StudentResponseDTO> searchStudents(String keyword) {
        log.debug("Searching students with keyword: {}", keyword);

        return studentRepository.searchStudents(keyword).stream()
                .map(student -> {
                    StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);

                    try {
                        UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());
                        response.setUserInfo(userInfo);
                    } catch (Exception e) {
                        log.warn("Could not fetch user info for student {}: {}", student.getId(), e.getMessage());
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsBySchool(Long schoolId) {
        log.debug("Fetching students by school ID: {}", schoolId);

        return studentRepository.findBySchoolId(schoolId).stream()
                .map(student -> {
                    StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);

                    try {
                        UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());
                        response.setUserInfo(userInfo);
                    } catch (Exception e) {
                        log.warn("Could not fetch user info for student {}: {}", student.getId(), e.getMessage());
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO changeEnrollmentStatus(Long studentId, EnrollmentStatus status) {
        log.info("Changing enrollment status for student {} to {}", studentId, status);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setEnrollmentStatus(status);

        // Update isActive based on status
        if (status == EnrollmentStatus.ACTIVE) {
            student.setIsActive(true);
        } else if (status == EnrollmentStatus.SUSPENDED || status == EnrollmentStatus.WITHDRAWN) {
            student.setIsActive(false);
        }

        Student updatedStudent = studentRepository.save(student);
        log.info("Student {} enrollment status changed to {}", studentId, status);

        return modelMapper.map(updatedStudent, StudentDTO.class);
    }

    @Override
    public StudentDTO activateStudent(Long studentId) {
        log.info("Activating student: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.activate();

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO deactivateStudent(Long studentId) {
        log.info("Deactivating student: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.deactivate();

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO suspendStudent(Long studentId) {
        log.info("Suspending student: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.suspend();

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO graduateStudent(Long studentId) {
        log.info("Graduating student: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!student.isEligibleForGraduation()) {
            throw new RuntimeException("Student is not eligible for graduation");
        }

        student.graduate();

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO updateAcademicInfo(Long studentId, AcademicUpdateDTO academicUpdate) {
        log.info("Updating academic info for student: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (academicUpdate.getGpa() != null) {
            student.setGpa(academicUpdate.getGpa());
        }
        if (academicUpdate.getTotalCredits() != null) {
            student.setTotalCredits(academicUpdate.getTotalCredits());
        }
        if (academicUpdate.getCompletedCredits() != null) {
            student.setCompletedCredits(academicUpdate.getCompletedCredits());
        }
        if (academicUpdate.getProgram() != null) {
            student.setProgram(academicUpdate.getProgram());
        }
        if (academicUpdate.getMajor() != null) {
            student.setMajor(academicUpdate.getMajor());
        }
        if (academicUpdate.getMinor() != null) {
            student.setMinor(academicUpdate.getMinor());
        }
        if (academicUpdate.getAcademicLevel() != null) {
            student.setAcademicLevel(academicUpdate.getAcademicLevel());
        }
        if (academicUpdate.getExpectedGraduationYear() != null) {
            student.setExpectedGraduationYear(academicUpdate.getExpectedGraduationYear());
        }

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO addCredits(Long studentId, Integer credits) {
        log.info("Adding {} credits to student: {}", credits, studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.addCredits(credits);

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentResponseDTO getStudentWithUserInfo(Long studentId) {
        log.debug("Fetching student with user info for ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);

        try {
            UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());
            response.setUserInfo(userInfo);
        } catch (Exception e) {
            log.error("Error fetching user info for student {}: {}", studentId, e.getMessage());
            throw new RuntimeException("Could not fetch user information");
        }

        return response;
    }

    @Override
    public StudentDTO syncWithUserService(Long studentId) {
        log.info("Syncing student {} with user service", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        try {
            // Fetch latest user info
            UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());

            // Update student info from user
            if (userInfo.getEmail() != null) {
                student.setEmail(userInfo.getEmail());
            }
            if (userInfo.getFirstName() != null) {
                student.setFirstName(userInfo.getFirstName());
            }
            if (userInfo.getLastName() != null) {
                student.setLastName(userInfo.getLastName());
            }
            if (userInfo.getProfilePicture() != null) {
                student.setProfilePicture(userInfo.getProfilePicture());
            }

        } catch (Exception e) {
            log.warn("Could not sync with user service for student {}: {}", studentId, e.getMessage());
        }

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public boolean validateStudentUser(Long studentId, Long userId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getUserId().equals(userId);
    }

    @Override
    public boolean studentExists(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public boolean userHasStudentProfile(Long userId) {
        return studentRepository.existsByUserId(userId);
    }

    @Override
    public boolean studentCodeExists(String studentCode) {
        return studentRepository.existsByStudentCode(studentCode);
    }

    @Override
    public boolean isStudentActive(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getIsActive() && student.getEnrollmentStatus() == EnrollmentStatus.ACTIVE;
    }

    @Override
    public Long getTotalStudentsCount() {
        return studentRepository.count();
    }

    @Override
    public Long getActiveStudentsCount() {
        return studentRepository.countByEnrollmentStatus(EnrollmentStatus.ACTIVE);
    }

    @Override
    public Long getGraduatedStudentsCount() {
        return studentRepository.countByEnrollmentStatus(EnrollmentStatus.GRADUATED);
    }

    @Override
    public Long getInternationalStudentsCount() {
        return studentRepository.countByIsInternationalTrue();
    }

    @Override
    public List<Object[]> getStudentsCountBySchool() {
        return studentRepository.countStudentsBySchool();
    }

    @Override
    public List<Object[]> getStudentsCountByProgram() {
        return studentRepository.countStudentsByProgram();
    }

    @Override
    public List<Object[]> getStudentsCountByEnrollmentStatus() {
        return studentRepository.countStudentsByEnrollmentStatus();
    }

    @Override
    public List<Object[]> getStudentsCountByAcademicLevel() {
        return studentRepository.countStudentsByAcademicLevel();
    }

    @Override
    public Double getAverageGPA() {
        return studentRepository.averageGPA();
    }

    @Override
    public Integer getTotalCreditsAcrossStudents() {
        return studentRepository.sumCompletedCredits();
    }

    @Override
    public void graduateEligibleStudents() {
        log.info("Graduating eligible students");

        List<Student> eligibleStudents = studentRepository.findEligibleForGraduation();

        eligibleStudents.forEach(student -> {
            student.graduate();
            log.info("Auto-graduated student: {}", student.getFullName());
        });

        studentRepository.saveAll(eligibleStudents);
        log.info("Graduated {} eligible students", eligibleStudents.size());
    }

    @Override
    public void updateAcademicLevels() {
        log.info("Updating academic levels for all students");

        List<Student> students = studentRepository.findAll();

        students.forEach(student -> {
            String newLevel = student.getCurrentAcademicLevel();
            if (!newLevel.equals(student.getAcademicLevel())) {
                student.setAcademicLevel(newLevel);
                log.debug("Updated academic level for student {}: {} -> {}",
                        student.getId(), student.getAcademicLevel(), newLevel);
            }
        });

        studentRepository.saveAll(students);
        log.info("Updated academic levels for {} students", students.size());
    }

    @Override
    public void deactivateInactiveStudents(LocalDate cutoffDate) {
        log.info("Deactivating inactive students since {}", cutoffDate);

        List<Student> inactiveStudents = studentRepository.findInactiveStudentsSince(cutoffDate);

        inactiveStudents.forEach(student -> {
            student.deactivate();
            log.info("Auto-deactivated student: {}", student.getFullName());
        });

        studentRepository.saveAll(inactiveStudents);
        log.info("Deactivated {} inactive students", inactiveStudents.size());
    }

    // Implementing other filtering methods
    @Override
    public List<StudentResponseDTO> getStudentsByProgram(String program) {
        return studentRepository.findByProgram(program).stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major).stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByEnrollmentStatus(EnrollmentStatus status) {
        return studentRepository.findByEnrollmentStatus(status).stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByAcademicLevel(String academicLevel) {
        return studentRepository.findByAcademicLevel(academicLevel).stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getActiveStudents() {
        return studentRepository.findByIsActiveTrue().stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getGraduatedStudents() {
        return studentRepository.findByIsGraduatedTrue().stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getInternationalStudents() {
        return studentRepository.findByIsInternationalTrue().stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByCommunity(Long communityId) {
        return studentRepository.findByCommunityId(communityId).stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByAdvisor(Long advisorId) {
        return studentRepository.findByAdvisorId(advisorId).stream()
                .map(student -> modelMapper.map(student, StudentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO updateGPA(Long studentId, Double gpa) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setGpa(gpa);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO updateProgram(Long studentId, String program, String major) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (program != null) student.setProgram(program);
        if (major != null) student.setMajor(major);

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }
}