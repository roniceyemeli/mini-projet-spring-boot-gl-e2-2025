package com.service.user.service;

import com.service.user.entity.Role;

import java.util.List;

public interface IServiceRole {

    // CRUD Operations
    public Role createRole(Role role);
    public Role updateRole(Integer id, Role role);
    public void deleteRole(Integer id);
    public Role getRoleById(Integer id);
    public Role getRoleByName(String name);
    public List<Role> getAllRoles();

    // Business Operations
    public List<Role> getDefaultRoles();
    public List<Role> searchRoles(String keyword);
    public boolean roleExists(String name);

    // Permission Management
    public Role addPermission(Integer roleId, String permission);
    public Role removePermission(Integer roleId, String permission);
    public boolean hasPermission(Integer roleId, String permission);

    // Default Role Management
    public Role setAsDefault(Integer roleId);
    public Role removeAsDefault(Integer roleId);
}