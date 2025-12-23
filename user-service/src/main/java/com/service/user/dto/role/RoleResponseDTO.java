package com.service.user.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private String permissions;
    private Boolean isDefault;
    private Boolean isSystem;
    private Integer userCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}