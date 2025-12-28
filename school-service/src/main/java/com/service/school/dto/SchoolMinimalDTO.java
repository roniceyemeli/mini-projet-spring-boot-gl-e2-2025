package com.service.school.dto;

import com.service.school.enums.SchoolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolMinimalDTO {
    private UUID id;
    private String name;
    private String slug;
    private String logoUrl;
    private SchoolType type;
    private Boolean isActive;
    private String city;
    private String country;
}

