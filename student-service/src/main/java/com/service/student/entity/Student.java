package com.service.student.entity;

import com.service.student.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students", schema = "student_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "student_code", unique = true, length = 50)
    private String studentCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender; // MALE, FEMALE, OTHER

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "citizenship", length = 100)
    private String citizenship;

    @Column(name = "id_card_number", unique = true, length = 50)
    private String idCardNumber;

    @Column(name = "passport_number", unique = true, length = 50)
    private String passportNumber;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "emergency_phone", length = 20)
    private String emergencyPhone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "personal_email", length = 100)
    private String personalEmail;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "program", length = 100)
    private String program; // e.g., "Computer Science", "Business Administration"

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "minor", length = 100)
    private String minor;

    @Column(name = "enrollment_year")
    private Integer enrollmentYear;

    @Column(name = "expected_graduation_year")
    private Integer expectedGraduationYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", length = 20)
    private EnrollmentStatus enrollmentStatus = EnrollmentStatus.ACTIVE;

    @Column(name = "academic_level", length = 50)
    private String academicLevel; // FRESHMAN, SOPHOMORE, JUNIOR, SENIOR, GRADUATE

    @Column(name = "gpa", precision = 3, scale = 2)
    private Double gpa;

    @Column(name = "total_credits")
    private Integer totalCredits = 0;

    @Column(name = "completed_credits")
    private Integer completedCredits = 0;

    @Column(name = "community_id")
    private Long communityId;

    @Column(name = "club_id")
    private Long clubId;

    @Column(name = "advisor_id")
    private Long advisorId;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;

    @Column(name = "is_international")
    private Boolean isInternational = false;

    @Column(name = "visa_status", length = 50)
    private String visaStatus;

    @Column(name = "financial_aid_status", length = 50)
    private String financialAidStatus; // NONE, PARTIAL, FULL

    @Column(name = "scholarship_name", length = 200)
    private String scholarshipName;

    @Column(name = "disabilities", columnDefinition = "TEXT")
    private String disabilities; // Comma-separated or JSON

    @Column(name = "special_needs", columnDefinition = "TEXT")
    private String specialNeeds;

    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;

    @Column(name = "emergency_contact_name", length = 200)
    private String emergencyContactName;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "emergency_contact_address", length = 500)
    private String emergencyContactAddress;

    @Column(name = "emergency_contact_email", length = 100)
    private String emergencyContactEmail;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_graduated")
    private Boolean isGraduated = false;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (enrollmentStatus == null) {
            enrollmentStatus = EnrollmentStatus.ACTIVE;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isGraduated == null) {
            isGraduated = false;
        }
        if (isInternational == null) {
            isInternational = false;
        }
        if (totalCredits == null) {
            totalCredits = 0;
        }
        if (completedCredits == null) {
            completedCredits = 0;
        }

        // Generate student code if not provided
        if (studentCode == null || studentCode.trim().isEmpty()) {
            studentCode = generateStudentCode();
        }

        // Generate full name if not provided
        if (fullName == null || fullName.trim().isEmpty()) {
            fullName = generateFullName();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Update full name if first or last name changed
        if (firstName != null || lastName != null) {
            fullName = generateFullName();
        }

        // Update graduation status
        if (isGraduated && graduationDate == null) {
            graduationDate = LocalDate.now();
        }
    }

    // Business logic methods
    public boolean isEligibleForGraduation() {
        return enrollmentStatus == EnrollmentStatus.ACTIVE &&
                !isGraduated &&
                expectedGraduationYear != null &&
                LocalDate.now().getYear() >= expectedGraduationYear &&
                completedCredits >= totalCredits;
    }

    public boolean canEnrollInCourse() {
        return enrollmentStatus == EnrollmentStatus.ACTIVE &&
                !isGraduated &&
                isActive;
    }

    public void graduate() {
        this.isGraduated = true;
        this.graduationDate = LocalDate.now();
        this.enrollmentStatus = EnrollmentStatus.GRADUATED;
    }

    public void suspend() {
        this.enrollmentStatus = EnrollmentStatus.SUSPENDED;
        this.isActive = false;
    }

    public void activate() {
        this.enrollmentStatus = EnrollmentStatus.ACTIVE;
        this.isActive = true;
    }

    public void deactivate() {
        this.enrollmentStatus = EnrollmentStatus.INACTIVE;
        this.isActive = false;
    }

    public void addCredits(Integer credits) {
        if (completedCredits == null) completedCredits = 0;
        completedCredits += credits;

        // Recalculate GPA if needed
        // This is a simplified version - in reality, GPA calculation is more complex
    }

    public String getCurrentAcademicLevel() {
        if (completedCredits == null) return "FRESHMAN";

        if (completedCredits >= 90) return "SENIOR";
        else if (completedCredits >= 60) return "JUNIOR";
        else if (completedCredits >= 30) return "SOPHOMORE";
        else return "FRESHMAN";
    }

    private String generateStudentCode() {
        // Format: YEAR-SCHOOLID-RANDOM (e.g., 2024-001-12345)
        String year = enrollmentYear != null ? enrollmentYear.toString() :
                String.valueOf(LocalDate.now().getYear());
        String schoolPrefix = schoolId != null ? String.format("%03d", schoolId) : "000";
        String random = String.format("%05d", (int)(Math.random() * 100000));

        return year + "-" + schoolPrefix + "-" + random;
    }

    private String generateFullName() {
        if (firstName == null && lastName == null) return null;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
}