package com.service.user.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String fullName;
    private String role;
    private String roleName;
    private Long roleId;
    private Long schoolId;
    private LocalDateTime expiresAt;
}