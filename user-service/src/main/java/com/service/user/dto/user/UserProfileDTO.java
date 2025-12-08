package com.service.user.dto.user;

import com.service.user.dto.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String profilePicture;
    private RoleDTO role;
    private Long schoolId;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    // For communication with other services
    private Object studentInfo;  // From student-service
    private Object schoolInfo;   // From school-service
}
