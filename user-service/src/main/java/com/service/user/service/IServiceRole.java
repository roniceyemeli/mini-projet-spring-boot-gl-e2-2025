package com.service.user.service;

import com.service.user.entity.Role;

import java.util.List;

public interface IServiceRole {

    // CRUD Operations
    public Role createRole(Role role);
    public Role updateRole(Long id, Role role);
    public void deleteRole(Long id);
    public Role getRoleById(Long id);
    public Role getRoleByName(String name);
    public List<Role> getAllRoles();

    // Business Operations
    public List<Role> getDefaultRoles();
    public List<Role> searchRoles(String keyword);
    public boolean roleExists(String name);

    // Permission Management
    public Role addPermission(Long roleId, String permission);
    public Role removePermission(Long roleId, String permission);
    public boolean hasPermission(Long roleId, String permission);

    // Default Role Management
    public Role setAsDefault(Long roleId);
    public Role removeAsDefault(Long roleId);
}