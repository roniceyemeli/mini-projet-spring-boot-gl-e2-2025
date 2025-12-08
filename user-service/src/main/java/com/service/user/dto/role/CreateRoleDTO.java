package com.service.user.dto.role;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDTO {

    @NotBlank(message = "Role name is required")
    private String name;

    private String description;
    private String permissions;
    private Boolean isDefault = false;
}