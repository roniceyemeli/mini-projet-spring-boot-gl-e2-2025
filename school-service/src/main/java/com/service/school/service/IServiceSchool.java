package com.service.school.service;

import com.service.school.entity.School;
import java.util.List;

public interface IServiceSchool {
    public School addSchool(School school);
    public List<School> allSchools();
}
