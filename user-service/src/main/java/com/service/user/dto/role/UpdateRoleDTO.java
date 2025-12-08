package com.service.user.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleDTO {
    private String name;
    private String description;
    private String permissions;
    private Boolean isDefault;
}