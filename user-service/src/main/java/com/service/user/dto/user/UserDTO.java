package com.service.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Pattern DTO - User DTOs
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Long roleId;
    private Long schoolId;
    private Long communityId;
}