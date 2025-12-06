package com.service.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Long roleId;
    private Long schoolId;
}
