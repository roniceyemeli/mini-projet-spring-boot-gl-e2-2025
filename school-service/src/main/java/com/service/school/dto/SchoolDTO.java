package com.service.school.dto;

import com.service.school.enums.SchoolStatus;
import com.service.school.enums.SchoolType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDTO {

    @NotBlank(message = "School name is required")
    private String name;

    private String title;
    private String description;
    private String slug;
    private String address;
    private String fullAddress;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String website;
    private String phoneNumber;
    private String faxNumber;
    private Integer foundingYear;
    private SchoolType type;
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
    private Long createdBy;
    private Long updatedBy;
}
