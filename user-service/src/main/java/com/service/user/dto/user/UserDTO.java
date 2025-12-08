package com.service.user.dto.user;

import com.service.user.dto.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Pattern DTO - User DTOs
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isActive;
    private Boolean isVerified;
    private String profilePicture;
    private RoleDTO role;
    private Long schoolId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}