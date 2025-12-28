package com.service.school.service;

import com.service.school.dto.SchoolMinimalDTO;
import com.service.school.entity.School;
import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;
import com.service.school.repository.SchoolRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceSchool implements IServiceSchool {

    private final SchoolRepository schoolRepository;

    @Override
    public School createSchool(School school) {
        log.info("Creating new school: {}", school.getName());

        // Validate email uniqueness
        if (school.getEmail() != null && schoolRepository.existsByEmail(school.getEmail())) {
            throw new RuntimeException("School with email '" + school.getEmail() + "' already exists");
        }

        // Validate slug uniqueness
        if (school.getSlug() != null && schoolRepository.existsBySlug(school.getSlug())) {
            throw new RuntimeException("School with slug '" + school.getSlug() + "' already exists");
        }

        School savedSchool = schoolRepository.save(school);
        log.info("School created successfully with ID: {}", savedSchool.getId());

        return savedSchool;
    }

    @Override
    public School updateSchool(UUID id, School school) {
        log.info("Updating school with ID: {}", id);

        School existingSchool = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found with id: " + id));

        // Check if email changed and is still unique
        if (school.getEmail() != null &&
                !school.getEmail().equals(existingSchool.getEmail()) &&
                schoolRepository.existsByEmail(school.getEmail())) {
            throw new RuntimeException("School with email '" + school.getEmail() + "' already exists");
        }

        // Check if slug changed and is still unique
        if (school.getSlug() != null &&
                !school.getSlug().equals(existingSchool.getSlug()) &&
                schoolRepository.existsBySlug(school.getSlug())) {
            throw new RuntimeException("School with slug '" + school.getSlug() + "' already exists");
        }

        // Update fields
        existingSchool.setName(school.getName());
        existingSchool.setTitle(school.getTitle());
        existingSchool.setDescription(school.getDescription());
        existingSchool.setSlug(school.getSlug());
        existingSchool.setAddress(school.getAddress());
        existingSchool.setFullAddress(school.getFullAddress());
        existingSchool.setEmail(school.getEmail());
        existingSchool.setWebsite(school.getWebsite());
        existingSchool.setPhoneNumber(school.getPhoneNumber());
        existingSchool.setFaxNumber(school.getFaxNumber());
        existingSchool.setFoundingYear(school.getFoundingYear());
        existingSchool.setType(school.getType());
        existingSchool.setAccreditationNumber(school.getAccreditationNumber());
        existingSchool.setTaxId(school.getTaxId());
        existingSchool.setRegistrationNumber(school.getRegistrationNumber());
        existingSchool.setLogoUrl(school.getLogoUrl());
        existingSchool.setBannerUrl(school.getBannerUrl());
        existingSchool.setCountry(school.getCountry());
        existingSchool.setCity(school.getCity());
        existingSchool.setPostalCode(school.getPostalCode());
        existingSchool.setLatitude(school.getLatitude());
        existingSchool.setIsPublic(school.getIsPublic());
        existingSchool.setTuitionRange(school.getTuitionRange());
        existingSchool.setAdmissionsEmail(school.getAdmissionsEmail());
        existingSchool.setAdmissionsPhone(school.getAdmissionsPhone());
        existingSchool.setContactPerson(school.getContactPerson());
        existingSchool.setContactPosition(school.getContactPosition());
        existingSchool.setFacebookUrl(school.getFacebookUrl());
        existingSchool.setTwitterUrl(school.getTwitterUrl());
        existingSchool.setLinkedinUrl(school.getLinkedinUrl());
        existingSchool.setInstagramUrl(school.getInstagramUrl());
        existingSchool.setRanking(school.getRanking());
        existingSchool.setAccreditationStatus(school.getAccreditationStatus());
        existingSchool.setAccreditationExpiryDate(school.getAccreditationExpiryDate());
        existingSchool.setMotto(school.getMotto());
        existingSchool.setVision(school.getVision());
        existingSchool.setMission(school.getMission());
        existingSchool.setIsActive(school.getIsActive());
        existingSchool.setUpdatedBy(school.getUpdatedBy());

        School updatedSchool = schoolRepository.save(existingSchool);
        log.info("School updated successfully: {}", updatedSchool.getName());

        return updatedSchool;
    }

    @Override
    public void deleteSchool(UUID id) {
        log.info("Deleting school with ID: {}", id);

        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found with id: " + id));

        // Check if school has students
        if (school.getTotalStudents() > 0) {
            throw new RuntimeException("Cannot delete school that has students. Deactivate it instead.");
        }

        schoolRepository.delete(school);
        log.info("School deleted successfully: {}", school.getName());
    }

    @Override
    public School getSchoolById(UUID id) {
        log.debug("Fetching school by ID: {}", id);

        return schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found with id: " + id));
    }

    @Override
    public Optional<School> getSchoolBySlug(String slug) {
        log.debug("Fetching school by slug: {}", slug);

        return schoolRepository.findBySlug(slug);
    }

    @Override
    public Optional<School> getSchoolByEmail(String email) {
        log.debug("Fetching school by email: {}", email);

        return schoolRepository.findByEmail(email);
    }

    @Override
    public List<School> getAllSchools() {
        log.debug("Fetching all schools");

        return schoolRepository.findAll();
    }

    @Override
    public List<School> searchSchools(String keyword) {
        log.debug("Searching schools with keyword: {}", keyword);

        return schoolRepository.searchSchools(keyword);
    }

    @Override
    public List<School> getSchoolsByType(SchoolType type) {
        log.debug("Fetching schools by type: {}", type);

        return schoolRepository.findByType(type);
    }

    @Override
    public List<School> getSchoolsByStatus(SchoolStatus status) {
        log.debug("Fetching schools by status: {}", status);

        return schoolRepository.findByStatus(status);
    }

    @Override
    public List<School> getSchoolsByCountry(String country) {
        log.debug("Fetching schools by country: {}", country);

        return schoolRepository.findByCountry(country);
    }

    @Override
    public List<School> getSchoolsByCity(String city) {
        log.debug("Fetching schools by city: {}", city);

        return schoolRepository.findByCity(city);
    }

    @Override
    public List<School> getFeaturedSchools() {
        log.debug("Fetching featured schools");

        return schoolRepository.findByIsFeaturedTrueAndStatus(SchoolStatus.ACTIVE);
    }

    @Override
    public List<School> getVerifiedSchools() {
        log.debug("Fetching verified schools");

        return schoolRepository.findByVerifiedTrueAndStatus(SchoolStatus.ACTIVE);
    }

    @Override
    public List<School> getActiveSchools() {
        log.debug("Fetching active schools");

        return schoolRepository.findByStatusAndIsActiveTrue(SchoolStatus.ACTIVE);
    }

    @Override
    public List<School> getPublicSchools() {
        log.debug("Fetching public schools");

        return schoolRepository.findByIsPublicTrueAndStatus(SchoolStatus.ACTIVE);
    }

    @Override
    public List<School> getPrivateSchools() {
        log.debug("Fetching private schools");

        return schoolRepository.findByIsPublicFalseAndStatus(SchoolStatus.ACTIVE);
    }

    @Override
    public School changeSchoolStatus(UUID schoolId, SchoolStatus status) {
        log.info("Changing school {} status to {}", schoolId, status);

        School school = getSchoolById(schoolId);
        school.setStatus(status);

        // Update isActive based on status
        if (status == SchoolStatus.ACTIVE) {
            school.setIsActive(true);
        } else if (status == SchoolStatus.SUSPENDED || status == SchoolStatus.CLOSED) {
            school.setIsActive(false);
        }

        School updatedSchool = schoolRepository.save(school);
        log.info("School {} status changed to {}", schoolId, status);

        return updatedSchool;
    }

    @Override
    public School activateSchool(UUID schoolId) {
        log.info("Activating school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.activate();

        return schoolRepository.save(school);
    }

    @Override
    public School deactivateSchool(UUID schoolId) {
        log.info("Deactivating school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.deactivate();

        return schoolRepository.save(school);
    }

    @Override
    public School suspendSchool(UUID schoolId) {
        log.info("Suspending school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.suspend();

        return schoolRepository.save(school);
    }

    @Override
    public School verifySchool(UUID schoolId) {
        log.info("Verifying school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.verify();

        return schoolRepository.save(school);
    }

    @Override
    public School setFeaturedStatus(UUID schoolId, boolean isFeatured) {
        log.info("Setting featured status for school {} to {}", schoolId, isFeatured);

        School school = getSchoolById(schoolId);

        // Only active and verified schools can be featured
        if (isFeatured && (!school.isOperational() || !school.getVerified())) {
            throw new RuntimeException("Only active and verified schools can be featured");
        }

        school.setIsFeatured(isFeatured);
        School updatedSchool = schoolRepository.save(school);

        log.info("School {} featured status set to {}", schoolId, isFeatured);

        return updatedSchool;
    }

    @Override
    public List<School> getTopFeaturedSchools(int limit) {
        log.debug("Fetching top {} featured schools", limit);

        return schoolRepository.findTopFeaturedSchools(limit);
    }

    @Override
    public School incrementStudentCount(UUID schoolId) {
        log.debug("Incrementing student count for school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.incrementStudentCount();

        return schoolRepository.save(school);
    }

    @Override
    public School decrementStudentCount(UUID schoolId) {
        log.debug("Decrementing student count for school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.decrementStudentCount();

        return schoolRepository.save(school);
    }

    @Override
    public School incrementTeacherCount(UUID schoolId) {
        log.debug("Incrementing teacher count for school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.incrementTeacherCount();

        return schoolRepository.save(school);
    }

    @Override
    public School decrementTeacherCount(UUID schoolId) {
        log.debug("Decrementing teacher count for school: {}", schoolId);

        School school = getSchoolById(schoolId);
        school.decrementTeacherCount();

        return schoolRepository.save(school);
    }

    @Override
    public School updateStudentCount(UUID schoolId, Integer count) {
        log.info("Updating student count for school {} to {}", schoolId, count);

        School school = getSchoolById(schoolId);
        school.setTotalStudents(count);

        return schoolRepository.save(school);
    }

    @Override
    public School updateTeacherCount(UUID schoolId, Integer count) {
        log.info("Updating teacher count for school {} to {}", schoolId, count);

        School school = getSchoolById(schoolId);
        school.setTotalTeachers(count);

        return schoolRepository.save(school);
    }

    @Override
    public boolean schoolExists(UUID schoolId) {
        return schoolRepository.existsById(schoolId);
    }

    @Override
    public boolean slugExists(String slug) {
        return schoolRepository.existsBySlug(slug);
    }

    @Override
    public boolean emailExists(String email) {
        return schoolRepository.existsByEmail(email);
    }

    @Override
    public boolean isSchoolOperational(UUID schoolId) {
        School school = getSchoolById(schoolId);
        return school.isOperational();
    }

    @Override
    public boolean isSchoolAccredited(UUID schoolId) {
        School school = getSchoolById(schoolId);
        return school.isAccredited();
    }

    @Override
    public boolean canSchoolAdmitStudents(UUID schoolId) {
        School school = getSchoolById(schoolId);
        return school.canAdmitStudents();
    }

    @Override
    public Long getTotalSchoolsCount() {
        return schoolRepository.count();
    }

    @Override
    public Long getActiveSchoolsCount() {
        return schoolRepository.countByStatusAndIsActiveTrue(SchoolStatus.ACTIVE);
    }

    @Override
    public Long getFeaturedSchoolsCount() {
        return schoolRepository.countByIsFeaturedTrueAndStatus(SchoolStatus.ACTIVE);
    }

    @Override
    public Long getVerifiedSchoolsCount() {
        return schoolRepository.countByVerifiedTrueAndStatus(SchoolStatus.ACTIVE);
    }

    @Override
    public List<Object[]> getSchoolsCountByType() {
        return schoolRepository.countSchoolsByType();
    }

    @Override
    public List<Object[]> getSchoolsCountByStatus() {
        return schoolRepository.countSchoolsByStatus();
    }

    @Override
    public List<Object[]> getSchoolsCountByCountry() {
        return schoolRepository.countSchoolsByCountry();
    }

    @Override
    public Integer getTotalStudentsAcrossSchools() {
        return schoolRepository.sumTotalStudents();
    }

    @Override
    public Integer getTotalTeachersAcrossSchools() {
        return schoolRepository.sumTotalTeachers();
    }

    @Override
    public List<School> getSchoolsNearLocation(Double latitude, Double longitude, Double radiusKm) {
        log.debug("Fetching schools near location: lat={}, long={}, radius={}km", latitude, longitude, radiusKm);

        // This is a simplified version - in production, you'd use PostGIS or similar
        return schoolRepository.findSchoolsNearLocation(latitude, longitude, radiusKm);
    }

    @Override
    public List<School> getSchoolsInRegion(String region) {
        log.debug("Fetching schools in region: {}", region);

        return schoolRepository.findByRegion(region);
    }

    @Override
    public void verifyAllPendingSchools() {
        log.info("Verifying all pending schools");

        List<School> pendingSchools = schoolRepository.findByStatus(SchoolStatus.PENDING_VERIFICATION);
        pendingSchools.forEach(school -> {
            school.verify();
            school.setStatus(SchoolStatus.ACTIVE);
        });

        schoolRepository.saveAll(pendingSchools);
        log.info("Verified {} pending schools", pendingSchools.size());
    }

    @Override
    public void deactivateInactiveSchools(LocalDateTime cutoffDate) {
        log.info("Deactivating inactive schools since {}", cutoffDate);

        List<School> inactiveSchools = schoolRepository.findInactiveSchoolsSince(cutoffDate);
        inactiveSchools.forEach(school -> {
            school.deactivate();
            school.setStatus(SchoolStatus.INACTIVE);
        });

        schoolRepository.saveAll(inactiveSchools);
        log.info("Deactivated {} inactive schools", inactiveSchools.size());
    }

    @Override
    public void updateAccreditationStatuses() {
        log.info("Updating accreditation statuses");

        List<School> schools = schoolRepository.findAll();
        schools.forEach(school -> {
            if (school.getAccreditationExpiryDate() != null &&
                    school.getAccreditationExpiryDate().isBefore(LocalDateTime.now())) {
                school.setAccreditationStatus("EXPIRED");
            }
        });

        schoolRepository.saveAll(schools);
        log.info("Updated accreditation statuses for {} schools", schools.size());
    }

    @Override
    public SchoolMinimalDTO getSchoolMinimalById(UUID id) {
        log.debug("Fetching minimal school by ID: {}", id);
        
        School school = getSchoolById(id);
        SchoolMinimalDTO dto = new SchoolMinimalDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setSlug(school.getSlug());
        dto.setLogoUrl(school.getLogoUrl());
        dto.setType(school.getType());
        dto.setIsActive(school.getIsActive());
        dto.setCity(school.getCity());
        dto.setCountry(school.getCountry());
        
        return dto;
    }
}