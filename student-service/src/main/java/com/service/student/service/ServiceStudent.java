package com.service.student.service;

import com.service.student.config.CommunityServiceClient;
import com.service.student.config.SchoolServiceClient;
import com.service.student.config.UserServiceClient;
import com.service.student.dto.request.AcademicUpdateDTO;
import com.service.student.dto.request.CreateStudentDTO;
import com.service.student.dto.request.UpdateStudentDTO;
import com.service.student.dto.response.StudentDTO;
import com.service.student.dto.response.StudentResponseDTO;
import com.service.student.entity.Student;
import com.service.student.enums.EnrollmentStatus;
import com.service.student.repository.StudentRepository;
import com.service.user.dto.user.UserMinimalDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceStudent implements IServiceStudent {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;
    private final CommunityServiceClient communityServiceClient;
    private final SchoolServiceClient schoolServiceClient;

    /* =======================
       CREATE & READ
       ======================= */

    @Override
    public StudentDTO createStudent(CreateStudentDTO studentDTO) {
        if (studentRepository.existsByUserId(studentDTO.getUserId())) {
            throw new RuntimeException("User already has a student profile");
        }

        if (studentDTO.getStudentCode() != null &&
                studentRepository.existsByStudentCode(studentDTO.getStudentCode())) {
            throw new RuntimeException("Student code already exists");
        }

        Student student = modelMapper.map(studentDTO, Student.class);
        student.setUserSyncStatus("PENDING");
        student.setAdvisorSyncStatus("PENDING");

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO getStudentById(UUID id) {
        return modelMapper.map(
                studentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Student not found: " + id)),
                StudentDTO.class
        );
    }

    @Override
    public StudentResponseDTO getStudentResponseById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));

        return toResponse(student);
    }

    @Override
    public StudentDTO getStudentByUserId(UUID userId) {
        return modelMapper.map(
                studentRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Student not found for user: " + userId)),
                StudentDTO.class
        );
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<StudentResponseDTO> searchStudents(String keyword) {
        return studentRepository.searchStudents(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsBySchool(UUID schoolId) {
        return studentRepository.findBySchoolId(schoolId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByProgram(String program) {
        return studentRepository.findByProgram(program).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByEnrollmentStatus(EnrollmentStatus status) {
        return studentRepository.findByEnrollmentStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByAcademicLevel(String academicLevel) {
        return studentRepository.findByAcademicLevel(academicLevel).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getActiveStudents() {
        return studentRepository.findByIsActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getGraduatedStudents() {
        return studentRepository.findByIsGraduatedTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getInternationalStudents() {
        return studentRepository.findByIsInternationalTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByCommunity(UUID communityId) {
        return studentRepository.findByCommunityId(communityId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> getStudentsByAdvisor(UUID advisorId) {
        return studentRepository.findByAdvisorId(advisorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public StudentResponseDTO getStudentWithUserInfo(UUID studentId) {
        Student student = getEntity(studentId);
        StudentResponseDTO response = toResponse(student);
        enrichWithUser(student, response);
        return response;
    }


    /* =======================
       UPDATE & DELETE
       ======================= */

    @Override
    public StudentDTO updateStudent(UUID id, UpdateStudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update only provided fields (partial update)
        if (dto.getFirstName() != null) student.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) student.setLastName(dto.getLastName());
        if (dto.getDateOfBirth() != null) student.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null) student.setGender(dto.getGender());
        if (dto.getNationality() != null) student.setNationality(dto.getNationality());
        if (dto.getCitizenship() != null) student.setCitizenship(dto.getCitizenship());
        if (dto.getIdCardNumber() != null) student.setIdCardNumber(dto.getIdCardNumber());
        if (dto.getPassportNumber() != null) student.setPassportNumber(dto.getPassportNumber());
        if (dto.getAddress() != null) student.setAddress(dto.getAddress());
        if (dto.getCity() != null) student.setCity(dto.getCity());
        if (dto.getState() != null) student.setState(dto.getState());
        if (dto.getCountry() != null) student.setCountry(dto.getCountry());
        if (dto.getPostalCode() != null) student.setPostalCode(dto.getPostalCode());
        if (dto.getPhoneNumber() != null) student.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmergencyPhone() != null) student.setEmergencyPhone(dto.getEmergencyPhone());
        if (dto.getPersonalEmail() != null) student.setPersonalEmail(dto.getPersonalEmail());
        if (dto.getSchoolId() != null) student.setSchoolId(dto.getSchoolId());
        if (dto.getProgram() != null) student.setProgram(dto.getProgram());
        if (dto.getMajor() != null) student.setMajor(dto.getMajor());
        if (dto.getMinor() != null) student.setMinor(dto.getMinor());
        if (dto.getEnrollmentYear() != null) student.setEnrollmentYear(dto.getEnrollmentYear());
        if (dto.getExpectedGraduationYear() != null) student.setExpectedGraduationYear(dto.getExpectedGraduationYear());
        if (dto.getCommunityId() != null) student.setCommunityId(dto.getCommunityId());
        if (dto.getClubId() != null) student.setClubId(dto.getClubId());
        if (dto.getAdvisorId() != null) {
            student.setAdvisorId(dto.getAdvisorId());
            student.setAdvisorSyncStatus("PENDING");
        }
        if (dto.getProfilePicture() != null) student.setProfilePicture(dto.getProfilePicture());
        if (dto.getResumeUrl() != null) student.setResumeUrl(dto.getResumeUrl());
        if (dto.getLinkedinUrl() != null) student.setLinkedinUrl(dto.getLinkedinUrl());
        if (dto.getGithubUrl() != null) student.setGithubUrl(dto.getGithubUrl());
        if (dto.getPortfolioUrl() != null) student.setPortfolioUrl(dto.getPortfolioUrl());
        if (dto.getIsInternational() != null) student.setIsInternational(dto.getIsInternational());
        if (dto.getVisaStatus() != null) student.setVisaStatus(dto.getVisaStatus());
        if (dto.getFinancialAidStatus() != null) student.setFinancialAidStatus(dto.getFinancialAidStatus());
        if (dto.getScholarshipName() != null) student.setScholarshipName(dto.getScholarshipName());
        if (dto.getDisabilities() != null) student.setDisabilities(dto.getDisabilities());
        if (dto.getSpecialNeeds() != null) student.setSpecialNeeds(dto.getSpecialNeeds());
        if (dto.getMedicalConditions() != null) student.setMedicalConditions(dto.getMedicalConditions());
        if (dto.getEmergencyContactName() != null) student.setEmergencyContactName(dto.getEmergencyContactName());
        if (dto.getEmergencyContactRelationship() != null) student.setEmergencyContactRelationship(dto.getEmergencyContactRelationship());
        if (dto.getEmergencyContactAddress() != null) student.setEmergencyContactAddress(dto.getEmergencyContactAddress());
        if (dto.getEmergencyContactEmail() != null) student.setEmergencyContactEmail(dto.getEmergencyContactEmail());
        if (dto.getIsActive() != null) student.setIsActive(dto.getIsActive());
        if (dto.getIsGraduated() != null) student.setIsGraduated(dto.getIsGraduated());
        if (dto.getNotes() != null) student.setNotes(dto.getNotes());

        // Update fullName if firstName or lastName changed
        if (dto.getFirstName() != null || dto.getLastName() != null) {
            String firstName = dto.getFirstName() != null ? dto.getFirstName() : student.getFirstName();
            String lastName = dto.getLastName() != null ? dto.getLastName() : student.getLastName();
            student.setFullName(firstName + " " + lastName);
        }

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public void deleteStudent(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.deactivate();
        student.setEnrollmentStatus(EnrollmentStatus.INACTIVE);
        studentRepository.save(student);
    }

    /* =======================
       STATUS & ACADEMICS
       ======================= */

    @Override
    public StudentDTO changeEnrollmentStatus(UUID studentId, EnrollmentStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setEnrollmentStatus(status);
        student.setIsActive(status == EnrollmentStatus.ACTIVE);

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO activateStudent(UUID studentId) {
        Student student = getEntity(studentId);
        student.activate();
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO deactivateStudent(UUID studentId) {
        Student student = getEntity(studentId);
        student.deactivate();
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO suspendStudent(UUID studentId) {
        Student student = getEntity(studentId);
        student.suspend();
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO graduateStudent(UUID studentId) {
        Student student = getEntity(studentId);

        if (!student.isEligibleForGraduation()) {
            throw new RuntimeException("Student is not eligible for graduation");
        }

        student.graduate();
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO updateAcademicInfo(UUID studentId, AcademicUpdateDTO dto) {
        Student student = getEntity(studentId);
        modelMapper.map(dto, student);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO addCredits(UUID studentId, Integer credits) {
        Student student = getEntity(studentId);
        student.addCredits(credits);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO updateGPA(UUID studentId, BigDecimal gpa) {
        Student student = getEntity(studentId);
        student.setGpa(gpa);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO updateProgram(UUID studentId, String program, String major) {
        Student student = getEntity(studentId);
        if (program != null) student.setProgram(program);
        if (major != null) student.setMajor(major);
        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    /* =======================
       SYNC
       ======================= */

    @Override
    public StudentDTO syncWithUserService(UUID studentId) {
        Student student = getEntity(studentId);

        try {
            UserMinimalDTO user = userServiceClient.getUserMinimalById(student.getUserId());
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setProfilePicture(user.getProfilePicture());
            student.markUserSynced();
        } catch (Exception e) {
            student.markUserSyncFailed();
        }

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    @Override
    public StudentDTO syncWithAdvisorService(UUID studentId) {
        Student student = getEntity(studentId);

        try {
            student.markAdvisorSynced();
        } catch (Exception e) {
            student.markAdvisorSyncFailed();
        }

        return modelMapper.map(studentRepository.save(student), StudentDTO.class);
    }

    /* =======================
       VALIDATION & STATS
       ======================= */

    @Override
    public boolean validateStudentUser(UUID studentId, UUID userId) {
        return getEntity(studentId).getUserId().equals(userId);
    }

    @Override
    public boolean studentExists(UUID studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public boolean userHasStudentProfile(UUID userId) {
        return studentRepository.existsByUserId(userId);
    }

    @Override
    public boolean isStudentActive(UUID studentId) {
        Student s = getEntity(studentId);
        return s.getIsActive() && s.getEnrollmentStatus() == EnrollmentStatus.ACTIVE;
    }

    @Override public long getTotalStudentsCount() { return studentRepository.count(); }
    @Override public long getActiveStudentsCount() { return studentRepository.countByEnrollmentStatus(EnrollmentStatus.ACTIVE); }
    @Override public long getGraduatedStudentsCount() { return studentRepository.countByEnrollmentStatus(EnrollmentStatus.GRADUATED); }
    @Override public long getInternationalStudentsCount() { return studentRepository.countByIsInternationalTrue(); }

    /* =======================
       INTERNAL HELPERS
       ======================= */

    private Student getEntity(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    private void enrichWithUser(Student student, StudentResponseDTO response) {
        if (student.getUserId() == null) {
            log.warn("Student {} has no userId, skipping user enrichment", student.getId());
            return;
        }
        
        try {
            log.debug("Fetching user info for userId: {}", student.getUserId());
            UserMinimalDTO userInfo = userServiceClient.getUserMinimalById(student.getUserId());
            if (userInfo != null) {
                response.setUserInfo(userInfo);
                log.debug("Successfully enriched student {} with user info", student.getId());
            } else {
                log.warn("User service returned null for userId: {}", student.getUserId());
            }
        } catch (Exception e) {
            log.error("Failed to fetch user info for student {} with userId {}: {}", 
                    student.getId(), student.getUserId(), e.getMessage(), e);
            // Continue without user info - student data is still valid
        }
    }

    private void calculateDerivedFields(Student student, StudentResponseDTO response) {
        if (student.getDateOfBirth() != null) {
            response.setAge(LocalDate.now().getYear() - student.getDateOfBirth().getYear());
        }

        if (student.getExpectedGraduationYear() != null) {
            response.setYearsUntilGraduation(
                    Math.max(0, student.getExpectedGraduationYear() - LocalDate.now().getYear())
            );
        }

        if (student.getTotalCredits() != null && student.getTotalCredits() > 0) {
            response.setCreditsPercentage(
                    Math.min(100.0,
                            (student.getCompletedCredits() * 100.0) / student.getTotalCredits())
            );
        }

        response.setEligibleForGraduation(student.isEligibleForGraduation());
        response.setUserReferenceValid(student.isUserReferenceValid());
        response.setAdvisorReferenceValid(student.isAdvisorReferenceValid());
    }
    @Override
    public boolean studentCodeExists(String studentCode) {
        return studentRepository.existsByStudentCode(studentCode);
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
    public BigDecimal getAverageGPA() {
        Double avg = studentRepository.averageGPA();
        return avg == null ? BigDecimal.ZERO : BigDecimal.valueOf(avg);
    }

    @Override
    public Integer getTotalCreditsAcrossStudents() {
        return studentRepository.sumCompletedCredits();
    }
    @Override
    public void graduateEligibleStudents() {
        List<Student> eligible = studentRepository.findEligibleForGraduation();
        eligible.forEach(Student::graduate);
        studentRepository.saveAll(eligible);
    }

    @Override
    public void updateAcademicLevels() {
        List<Student> students = studentRepository.findAll();
        students.forEach(Student::recalculateAcademicLevel);
        studentRepository.saveAll(students);
    }

    @Override
    public void deactivateInactiveStudents(LocalDate cutoffDate) {
        List<Student> inactive = studentRepository.findInactiveStudentsSince(cutoffDate);
        inactive.forEach(Student::deactivate);
        studentRepository.saveAll(inactive);
    }
    private StudentResponseDTO toResponse(Student student) {
        StudentResponseDTO response = modelMapper.map(student, StudentResponseDTO.class);
        enrichWithUser(student, response);
        enrichWithCommunity(student, response);
        enrichWithSchool(student, response);
        calculateDerivedFields(student, response);
        return response;
    }

    private void enrichWithCommunity(Student student, StudentResponseDTO response) {
        if (student.getCommunityId() == null) {
            log.debug("Student {} has no communityId, skipping community enrichment", student.getId());
            return;
        }
        
        try {
            log.debug("Fetching community info for communityId: {}", student.getCommunityId());
            response.setCommunityInfo(communityServiceClient.getCommunityMinimalById(student.getCommunityId()));
            log.debug("Successfully enriched student {} with community info", student.getId());
        } catch (Exception e) {
            log.error("Failed to fetch community info for student {} with communityId {}: {}", 
                    student.getId(), student.getCommunityId(), e.getMessage(), e);
            // Continue without community info - student data is still valid
        }
    }

    private void enrichWithSchool(Student student, StudentResponseDTO response) {
        if (student.getSchoolId() == null) {
            log.debug("Student {} has no schoolId, skipping school enrichment", student.getId());
            return;
        }
        
        try {
            log.debug("Fetching school info for schoolId: {}", student.getSchoolId());
            response.setSchoolInfo(schoolServiceClient.getSchoolMinimalById(student.getSchoolId()));
            log.debug("Successfully enriched student {} with school info", student.getId());
        } catch (Exception e) {
            log.error("Failed to fetch school info for student {} with schoolId {}: {}", 
                    student.getId(), student.getSchoolId(), e.getMessage(), e);
            // Continue without school info - student data is still valid
        }
    }

}
