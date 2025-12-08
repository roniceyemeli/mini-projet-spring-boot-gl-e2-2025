package com.service.user.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMinimalDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String profilePicture;
}