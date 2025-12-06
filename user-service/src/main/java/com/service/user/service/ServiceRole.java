package com.service.user.service;

import com.service.user.entity.Role;
import com.service.user.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ServiceRole implements IServiceRole {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        // Validate role name uniqueness
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role with name '" + role.getName() + "' already exists");
        }

        // Initialize collections
        role.setUsers(new HashSet<>());

        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Integer id, Role role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if name changed and is still unique
        if (!existingRole.getName().equals(role.getName()) &&
                roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role with name '" + role.getName() + "' already exists");
        }

        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole.setPermissions(role.getPermissions());
        existingRole.setIsDefault(role.getIsDefault());
//        existingRole.setUpdatedAt(LocalDateTime.now());

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if role has users
        if (!role.getUsers().isEmpty()) {
            throw new RuntimeException("Cannot delete role that has assigned users");
        }

        roleRepository.delete(role);
    }

    @Override
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getDefaultRoles() {
        return roleRepository.findByIsDefaultTrue();
    }

    @Override
    public List<Role> searchRoles(String keyword) {
        return roleRepository.searchRoles(keyword);
    }

    @Override
    public boolean roleExists(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role addPermission(Integer roleId, String permission) {
        Role role = getRoleById(roleId);

        String currentPermissions = role.getPermissions();
        Set<String> permissions = new HashSet<>();

        if (currentPermissions != null && !currentPermissions.isEmpty()) {
            permissions.addAll(Arrays.asList(currentPermissions.split(",")));
        }

        permissions.add(permission.trim());
        role.setPermissions(String.join(",", permissions));

        return roleRepository.save(role);
    }

    @Override
    public Role removePermission(Integer roleId, String permission) {
        Role role = getRoleById(roleId);

        String currentPermissions = role.getPermissions();
        if (currentPermissions == null || currentPermissions.isEmpty()) {
            return role;
        }

        Set<String> permissions = new HashSet<>(Arrays.asList(currentPermissions.split(",")));
        permissions.remove(permission.trim());

        role.setPermissions(permissions.isEmpty() ? null : String.join(",", permissions));

        return roleRepository.save(role);
    }

    @Override
    public boolean hasPermission(Integer roleId, String permission) {
        Role role = getRoleById(roleId);

        if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
            return false;
        }

        Set<String> permissions = Arrays.stream(role.getPermissions().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        return permissions.contains(permission.trim());
    }

    @Override
    public Role setAsDefault(Integer roleId) {
        Role role = getRoleById(roleId);
        role.setIsDefault(true);
        return roleRepository.save(role);
    }

    @Override
    public Role removeAsDefault(Integer roleId) {
        Role role = getRoleById(roleId);
        role.setIsDefault(false);
        return roleRepository.save(role);
    }
}