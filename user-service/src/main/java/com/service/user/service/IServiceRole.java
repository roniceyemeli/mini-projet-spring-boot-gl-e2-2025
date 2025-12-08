package com.service.user.service;

import com.service.user.dto.role.*;
import com.service.user.entity.Role;

import java.util.List;

public interface IServiceRole {

    // CRUD Operations
    RoleDTO createRole(CreateRoleDTO createRoleDTO);
    RoleDTO updateRole(Long id, UpdateRoleDTO updateRoleDTO);
    void deleteRole(Long id);
    RoleDTO getRoleById(Long id);
    RoleDTO getRoleByName(String name);
    RoleResponseDTO getRoleResponseById(Long id);
    List<RoleDTO> getAllRoles();
    List<RoleResponseDTO> getAllRoleResponses();

    // Search & Filter
    List<RoleDTO> searchRoles(String keyword);
    List<RoleDTO> getDefaultRoles();
    List<RoleDTO> getCustomRoles();
    List<RoleResponseDTO> getRolesWithUserCount();

    // Permission Management
    RoleDTO addPermission(Long roleId, String permission);
    RoleDTO removePermission(Long roleId, String permission);
    boolean hasPermission(Long roleId, String permission);
    List<String> getRolePermissions(Long roleId);

    // Default Role Management
    RoleDTO setAsDefault(Long roleId);
    RoleDTO removeAsDefault(Long roleId);

    // Validation
    boolean roleExists(Long roleId);
    boolean roleNameExists(String name);

    // Business Logic
    RoleDTO getOrCreateDefaultUserRole();
    void initializeDefaultRoles();
}