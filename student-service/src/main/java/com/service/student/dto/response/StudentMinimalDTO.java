package com.service.student.dto.response;

import com.service.student.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentMinimalDTO {
    private Long id;
    private Long userId;
    private String studentCode;
    private String fullName;
    private String program;
    private String major;
    private EnrollmentStatus enrollmentStatus;
    private String profilePicture;
}