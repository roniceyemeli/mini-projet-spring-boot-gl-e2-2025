package com.service.school.entity;

import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schools", schema = "school_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, length = 255)
    private String slug;

    @Column(length = 500)
    private String address;

    @Column(length = 500)
    private String fullAddress;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String website;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "fax_number", length = 20)
    private String faxNumber;

    @Column(name = "founding_year")
    private Integer foundingYear;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SchoolType type = SchoolType.UNIVERSITY;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SchoolStatus status = SchoolStatus.ACTIVE;

    @Column(name = "accreditation_number", length = 100)
    private String accreditationNumber;

    @Column(name = "tax_id", length = 50)
    private String taxId;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "total_students")
    private Integer totalStudents = 0;

    @Column(name = "total_teachers")
    private Integer totalTeachers = 0;

    @Column(name = "total_staff")
    private Integer totalStaff = 0;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "tuition_range", length = 100)
    private String tuitionRange;

    @Column(name = "admissions_email", length = 100)
    private String admissionsEmail;

    @Column(name = "admissions_phone", length = 20)
    private String admissionsPhone;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "contact_position", length = 100)
    private String contactPosition;

    @Column(name = "facebook_url", length = 500)
    private String facebookUrl;

    @Column(name = "twitter_url", length = 500)
    private String twitterUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "instagram_url", length = 500)
    private String instagramUrl;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "accreditation_status", length = 50)
    private String accreditationStatus;

    @Column(name = "accreditation_expiry_date")
    private LocalDateTime accreditationExpiryDate;

    @Column(name = "motto", length = 500)
    private String motto;

    @Column(name = "vision", columnDefinition = "TEXT")
    private String vision;

    @Column(name = "mission", columnDefinition = "TEXT")
    private String mission;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by",columnDefinition = "uuid")
    private UUID createdBy;

    @Column(name = "updated_by",columnDefinition = "uuid")
    private UUID updatedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = SchoolStatus.ACTIVE;
        }
        if (type == null) {
            type = SchoolType.UNIVERSITY;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isFeatured == null) {
            isFeatured = false;
        }
        if (verified == null) {
            verified = false;
        }

        // Generate slug from name if not provided
        if (slug == null || slug.trim().isEmpty()) {
            slug = generateSlug(name);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public boolean isOperational() {
        return status == SchoolStatus.ACTIVE && isActive;
    }

    public boolean isAccredited() {
        return accreditationStatus != null &&
                "ACCREDITED".equals(accreditationStatus) &&
                (accreditationExpiryDate == null ||
                        accreditationExpiryDate.isAfter(LocalDateTime.now()));
    }

    public boolean canAdmitStudents() {
        return isOperational() && isAccredited();
    }

    public void incrementStudentCount() {
        if (totalStudents == null) {
            totalStudents = 0;
        }
        totalStudents++;
    }

    public void decrementStudentCount() {
        if (totalStudents == null || totalStudents <= 0) {
            totalStudents = 0;
        } else {
            totalStudents--;
        }
    }

    public void incrementTeacherCount() {
        if (totalTeachers == null) {
            totalTeachers = 0;
        }
        totalTeachers++;
    }

    public void decrementTeacherCount() {
        if (totalTeachers == null || totalTeachers <= 0) {
            totalTeachers = 0;
        } else {
            totalTeachers--;
        }
    }

    public void verify() {
        this.verified = true;
        this.verificationDate = LocalDateTime.now();
    }

    public void suspend() {
        this.status = SchoolStatus.SUSPENDED;
        this.isActive = false;
    }

    public void activate() {
        this.status = SchoolStatus.ACTIVE;
        this.isActive = true;
    }

    public void deactivate() {
        this.status = SchoolStatus.INACTIVE;
        this.isActive = false;
    }

    private String generateSlug(String name) {
        if (name == null) return "";
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}