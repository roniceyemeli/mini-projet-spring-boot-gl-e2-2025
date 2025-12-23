package com.service.user.service;

import com.service.user.dto.role.*;

import java.util.List;
import java.util.UUID;

public interface IServiceRole {

    // CRUD Operations
    RoleDTO createRole(CreateRoleDTO createRoleDTO);
    RoleDTO updateRole(UUID id, UpdateRoleDTO updateRoleDTO);
    void deleteRole(UUID id);
    RoleDTO getRoleById(UUID id);
    RoleDTO getRoleByName(String name);
    RoleResponseDTO getRoleResponseById(UUID id);
    List<RoleDTO> getAllRoles();
    List<RoleResponseDTO> getAllRoleResponses();

    // Search & Filter
    List<RoleDTO> searchRoles(String keyword);
    List<RoleDTO> getDefaultRoles();
    List<RoleDTO> getCustomRoles();
    List<RoleResponseDTO> getRolesWithUserCount();

    // Permission Management
    RoleDTO addPermission(UUID roleId, String permission);
    RoleDTO removePermission(UUID roleId, String permission);
    boolean hasPermission(UUID roleId, String permission);
    List<String> getRolePermissions(UUID roleId);

    // Default Role Management
    RoleDTO setAsDefault(UUID roleId);
    RoleDTO removeAsDefault(UUID roleId);

    // Validation
    boolean roleExists(UUID roleId);
    boolean roleNameExists(String name);

    // Business Logic
    RoleDTO getOrCreateDefaultUserRole();
    void initializeDefaultRoles();
}