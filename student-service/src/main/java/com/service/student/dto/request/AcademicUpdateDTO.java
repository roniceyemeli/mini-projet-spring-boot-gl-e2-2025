package com.service.student.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicUpdateDTO {
    private Double gpa;
    private Integer totalCredits;
    private Integer completedCredits;
    private String program;
    private String major;
    private String minor;
    private String academicLevel;
    private Integer expectedGraduationYear;
}