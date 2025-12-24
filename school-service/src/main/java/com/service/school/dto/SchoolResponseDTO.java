package com.service.school.dto;

import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolResponseDTO {

    private UUID id;
    private String name;
    private String title;
    private String description;
    private String slug;
    private String address;
    private String fullAddress;
    private String email;
    private String website;
    private String phoneNumber;
    private String faxNumber;
    private Integer foundingYear;
    private SchoolType type;
    private SchoolStatus status;
    private String accreditationNumber;
    private String taxId;
    private String registrationNumber;
    private String logoUrl;
    private String bannerUrl;
    private String country;
    private String city;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private Integer totalStudents;
    private Integer totalTeachers;
    private Integer totalStaff;
    private Boolean isPublic;
    private String tuitionRange;
    private String admissionsEmail;
    private String admissionsPhone;
    private String contactPerson;
    private String contactPosition;
    private String facebookUrl;
    private String twitterUrl;
    private String linkedinUrl;
    private String instagramUrl;
    private Boolean isFeatured;
    private Integer ranking;
    private String accreditationStatus;
    private LocalDateTime accreditationExpiryDate;
    private String motto;
    private String vision;
    private String mission;
    private Boolean isActive;
    private Boolean verified;
    private LocalDateTime verificationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    // Calculated fields
    private boolean operational;
    private boolean accredited;
    private boolean canAdmitStudents;
}