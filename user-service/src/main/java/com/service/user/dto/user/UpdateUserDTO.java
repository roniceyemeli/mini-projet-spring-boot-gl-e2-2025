package com.service.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private Long roleId;
    private Long schoolId;
}
