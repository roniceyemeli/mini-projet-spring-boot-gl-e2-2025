package com.service.user.dto.role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String permissions;
    private Boolean isDefault;
    private Boolean isSystem;
    private Integer userCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}