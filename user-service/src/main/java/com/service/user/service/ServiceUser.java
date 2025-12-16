package com.service.user.service;

import com.service.user.dto.user.*;
import com.service.user.entity.User;
import com.service.user.entity.Role;
import com.service.user.repository.UserRepository;
import com.service.user.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceUser implements IServiceUser {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        log.info("Creating new user: {}", createUserDTO.getEmail());

        // Validate email uniqueness
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new RuntimeException("User with email '" + createUserDTO.getEmail() + "' already exists");
        }

        // Map DTO to Entity
        User user = modelMapper.map(createUserDTO, User.class);
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        // Set role if provided
        if (createUserDTO.getRoleId() != null) {
            Role role = roleRepository.findById(createUserDTO.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        } else {
            // Assign default role
            Role defaultRole = roleRepository.findByIsDefaultTrue().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No default role found"));
            user.setRole(defaultRole);
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserResponseDTO getUserResponseById(Long id) {
        log.debug("Fetching user response by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserResponseDTO response = modelMapper.map(user, UserResponseDTO.class);
        response.setFullName(user.getFullName());

        return response;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.debug("Fetching all users");

        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update fields if provided
        if (updateUserDTO.getFirstName() != null) {
            user.setFirstName(updateUserDTO.getFirstName());
        }
        if (updateUserDTO.getLastName() != null) {
            user.setLastName(updateUserDTO.getLastName());
        }
        if (updateUserDTO.getPhone() != null) {
            user.setPhone(updateUserDTO.getPhone());
        }
        if (updateUserDTO.getRoleId() != null) {
            Role role = roleRepository.findById(updateUserDTO.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }
        if (updateUserDTO.getSchoolId() != null) {
            user.setSchoolId(updateUserDTO.getSchoolId());
        }
        if (updateUserDTO.getIsActive() != null) {
            user.setIsActive(updateUserDTO.getIsActive());
        }
        if (updateUserDTO.getProfilePicture() != null) {
            user.setProfilePicture(updateUserDTO.getProfilePicture());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getEmail());

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Soft delete (deactivate) instead of hard delete
        user.setIsActive(false);
        userRepository.save(user);

        log.info("User deactivated: {}", user.getEmail());
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        log.debug("Fetching user profile for ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDTO profile = modelMapper.map(user, UserProfileDTO.class);
        profile.setFullName(user.getFullName());

        // TODO: Implement communication with other services
        // profile.setStudentInfo(studentServiceClient.getStudentByUserId(userId));
        // profile.setSchoolInfo(schoolServiceClient.getSchoolById(user.getSchoolId()));

        return profile;
    }

    @Override
    public UserDTO activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(true);
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public UserDTO deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public UserDTO verifyUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsVerified(true);
        user.setVerificationToken(null);
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateLastLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLogin(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public UserDTO authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is deactivated");
        }

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmailForAuth(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }

    @Override
    public List<UserResponseDTO> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword).stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(Long roleId) {
        return userRepository.findByRoleId(roleId).stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUsersBySchool(Long schoolId) {
        return userRepository.findBySchoolId(schoolId).stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getVerifiedUsers() {
        return userRepository.findByIsVerifiedTrue().stream()
                .map(user -> {
                    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
                    dto.setFullName(user.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMinimalDTO> getAllMinimalUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserMinimalDTO dto = new UserMinimalDTO();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setFullName(user.getFullName());
                    dto.setProfilePicture(user.getProfilePicture());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Long getTotalUsersCount() {
        return userRepository.count();
    }

    @Override
    public Long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }

    @Override
    public Long getVerifiedUsersCount() {
        return (long) userRepository.findByIsVerifiedTrue().size();
    }

    @Override
    public List<Object[]> getUsersCountByRole() {
        // Implementation would require custom query
        return List.of();
    }
}