package com.service.user.service;


import com.service.user.dto.user.*;
import com.service.user.entity.User;
import com.service.user.repository.RoleRepository;
import com.service.user.repository.UserRepository;
import com.service.user.utils.UserMapperConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private ServiceStudent studentServiceClient;

    @Autowired
    private UserMapperConfig userMapper;

    // User CRUD Operations
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(createUserDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());

        if (user.getRoleId() != null) {
            roleRepository.findById(user.getRoleId())
                    .ifPresent(role -> response.setRole(userMapper.toDTO(role)));
        }

        if (user.getSchoolId() != null) {
            schoolRepository.findById(user.getSchoolId())
                    .ifPresent(school -> response.setSchool(userMapper.toDTO(school)));
        }

        return response;
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> getUserById(user.getId()))
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntityFromDTO(updateUserDTO, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    // Communication synchrone avec student-service
    public UserProfileDTO getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDTO profile = new UserProfileDTO();
        profile.setId(user.getId());
        profile.setEmail(user.getEmail());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());

        if (user.getRoleId() != null) {
            roleRepository.findById(user.getRoleId())
                    .ifPresent(role -> profile.setRole(userMapper.toDTO(role)));
        }

        if (user.getSchoolId() != null) {
            schoolRepository.findById(user.getSchoolId())
                    .ifPresent(school -> profile.setSchool(userMapper.toDTO(school)));
        }

        // Communication synchrone avec student-service via Feign
        try {
            StudentInfoDTO studentInfo = studentServiceClient.getStudentByUserId(id);
            profile.setStudentInfo(studentInfo);
        } catch (Exception e) {
            // Student info not found or service unavailable
            profile.setStudentInfo(null);
        }

        return profile;
    }
}
