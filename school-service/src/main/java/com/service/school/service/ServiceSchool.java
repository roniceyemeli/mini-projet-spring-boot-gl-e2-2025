package com.service.school.service;


import com.service.school.entity.School;
import com.service.school.repository.SchoolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceSchool implements IServiceSchool {
    SchoolRepository schoolRepository;
    @Override
    public School addSchool(School event) {
        return schoolRepository.save(event);
    }

    @Override
    public List<School> allSchools() {
        return schoolRepository.findAll();
    }
}
