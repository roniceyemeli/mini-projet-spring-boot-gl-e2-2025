package com.service.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private UUID roleId;
    private UUID schoolId;
    private Boolean isActive;
    private String profilePicture;
}