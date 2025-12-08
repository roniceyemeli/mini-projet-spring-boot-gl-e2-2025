package com.service.student.dto.response;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {
    private Long id;
    private Long userId;
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
    private String email;
    private Long schoolId;
    private String program;
    private String major;
    private EnrollmentStatus enrollmentStatus;
    private String academicLevel;
    private Double gpa;
    private Integer completedCredits;
    private Integer totalCredits;
    private Long communityId;
    private Long advisorId;
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
}