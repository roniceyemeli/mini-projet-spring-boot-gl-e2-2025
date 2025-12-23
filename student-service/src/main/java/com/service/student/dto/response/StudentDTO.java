package com.service.student.dto.response;

import com.service.student.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private UUID userId;
    private String studentCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String citizenship;
    private String idCardNumber;
    private String passportNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private String emergencyPhone;
    private String personalEmail;
    private UUID schoolId;
    private String program;
    private String major;
    private String minor;
    private Integer enrollmentYear;
    private Integer expectedGraduationYear;
    private EnrollmentStatus enrollmentStatus;
    private String academicLevel;
    private BigDecimal gpa;
    private Integer totalCredits;
    private Integer completedCredits;
    private UUID communityId;
    private UUID clubId;
    private UUID advisorId;
    private String profilePicture;
    private String resumeUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private Boolean isInternational;
    private String visaStatus;
    private String financialAidStatus;
    private String scholarshipName;
    private String disabilities;
    private String specialNeeds;
    private String medicalConditions;
    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactAddress;
    private String emergencyContactEmail;
    private Boolean isActive;
    private Boolean isGraduated;
    private LocalDate graduationDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    // Sync status fields
    private String userSyncStatus;
    private String advisorSyncStatus;
    private LocalDateTime lastSyncAttempt;
}