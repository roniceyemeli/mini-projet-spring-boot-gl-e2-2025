package com.service.school.service;


import com.service.school.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceSchool {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserMapper userMapper;

    public SchoolDTO createSchool(CreateSchoolDTO createSchoolDTO) {
        School school = userMapper.toEntity(createSchoolDTO);
        School savedSchool = schoolRepository.save(school);
        return userMapper.toDTO(savedSchool);
    }

    public SchoolDTO getSchoolById(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));
        return userMapper.toDTO(school);
    }

    public List<SchoolDTO> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteSchool(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new RuntimeException("School not found");
        }
        schoolRepository.deleteById(id);
    }
}