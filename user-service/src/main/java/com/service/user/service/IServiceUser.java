package com.service.user.service;

import com.service.user.dto.user.*;
import com.service.user.entity.User;

import java.util.List;

public interface IServiceUser {

    // CRUD Operations
    UserDTO createUser(CreateUserDTO createUserDTO);
    UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    UserResponseDTO getUserResponseById(Long id);
    List<UserResponseDTO> getAllUsers();
    List<UserMinimalDTO> getAllMinimalUsers();

    // Search & Filter
    List<UserResponseDTO> searchUsers(String keyword);
    List<UserResponseDTO> getUsersByRole(Long roleId);
    List<UserResponseDTO> getUsersBySchool(Long schoolId);
    List<UserResponseDTO> getActiveUsers();
    List<UserResponseDTO> getVerifiedUsers();

    // Profile & Extended Info
    UserProfileDTO getUserProfile(Long userId);

    // User Management
    UserDTO activateUser(Long userId);
    UserDTO deactivateUser(Long userId);
    UserDTO verifyUser(Long userId);
    UserDTO updateLastLogin(Long userId);

    // Validation
    boolean userExists(Long userId);
    boolean emailExists(String email);

    // Statistics
    Long getTotalUsersCount();
    Long getActiveUsersCount();
    Long getVerifiedUsersCount();
    List<Object[]> getUsersCountByRole();

    // For Auth Service
    UserDTO authenticateUser(String email, String password);
    UserDTO getUserByEmailForAuth(String email);
}