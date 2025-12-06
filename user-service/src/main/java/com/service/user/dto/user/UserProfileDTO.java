package com.service.user.dto.user;

import com.service.user.dto.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private RoleDTO role;
//    private SchoolDTO school;
//    private StudentInfoDTO studentInfo;
}
