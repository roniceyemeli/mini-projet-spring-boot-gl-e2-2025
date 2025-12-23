package com.service.student.dto.response;

import com.service.student.enums.EnrollmentStatus;
import com.service.user.dto.user.UserMinimalDTO;
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
public class StudentResponseDTO {
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
    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private String personalEmail;
    private UUID schoolId;
    private String program;
    private String major;
    private EnrollmentStatus enrollmentStatus;
    private String academicLevel;
    private BigDecimal gpa;
    private Integer completedCredits;
    private Integer totalCredits;
    private UUID communityId;
    private UUID advisorId;
    private String profilePicture;
    private Boolean isInternational;
    private Boolean isActive;
    private Boolean isGraduated;
    private LocalDateTime createdAt;

    // Communication with user-service
    private UserMinimalDTO userInfo;

    // Calculated fields
    private Integer age;
    private Integer yearsUntilGraduation;
    private Double creditsPercentage;
    private boolean eligibleForGraduation;

    // Sync status indicators
    private boolean userReferenceValid;
    private boolean advisorReferenceValid;
}