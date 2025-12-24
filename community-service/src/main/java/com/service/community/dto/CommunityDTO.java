package com.service.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {
    private UUID id;

    @NotBlank(message = "Community title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 100, message = "Slug cannot exceed 100 characters")
    private String slug;

    @Size(max = 255, message = "Website URL cannot exceed 255 characters")
    private String website;

    @Email(message = "Contact email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String contactEmail;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String contactPhone;

    private Integer foundingYear;
    private Integer memberCount;
    private Boolean isActive;
    private String logoUrl;
    private UUID createdBy;
}