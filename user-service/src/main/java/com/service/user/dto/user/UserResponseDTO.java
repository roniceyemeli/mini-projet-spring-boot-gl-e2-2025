package com.service.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
//    private RoleDTO role;
//    private SchoolDTO school;
//    private LocalDateTime createdAt;
}