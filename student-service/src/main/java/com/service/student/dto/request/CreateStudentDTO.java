package com.service.student.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentDTO {

    @NotNull(message = "User UUID is required")
    private UUID userId;

    private String studentCode;
    private String firstName;
    private String lastName;
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
    private String notes;
    private Long createdBy;
}