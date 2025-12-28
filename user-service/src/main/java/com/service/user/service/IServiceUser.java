package com.service.user.service;

import com.service.user.dto.user.*;

import java.util.List;
import java.util.UUID;

public interface IServiceUser {

    // CRUD Operations
    UserDTO createUser(CreateUserDTO createUserDTO);
    UserDTO updateUser(UUID id, UpdateUserDTO updateUserDTO);
    void deleteUser(UUID id);
    UserDTO getUserById(UUID id);
    UserDTO getUserByEmail(String email);
    UserResponseDTO getUserResponseById(UUID id);
    List<UserResponseDTO> getAllUsers();
    List<UserMinimalDTO> getAllMinimalUsers();
    UserMinimalDTO getUserMinimalById(UUID id);
    UserMinimalDTO getUserMinimalByEmail(String email);

    // Search & Filter
    List<UserResponseDTO> searchUsers(String keyword);
    List<UserResponseDTO> getUsersByRole(UUID roleId);
    List<UserResponseDTO> getActiveUsers();
    List<UserResponseDTO> getVerifiedUsers();

    // Profile & Extended Info
    UserProfileDTO getUserProfile(UUID userId);

    // User Management
    UserDTO activateUser(UUID userId);
    UserDTO deactivateUser(UUID userId);
    UserDTO verifyUser(UUID userId);
    UserDTO updateLastLogin(UUID userId);

    // Validation
    boolean userExists(UUID userId);
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