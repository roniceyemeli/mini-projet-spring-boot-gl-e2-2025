package com.service.user.service;

import com.service.user.dto.role.*;
import com.service.user.entity.Role;
import com.service.user.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceRole implements IServiceRole {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoleDTO createRole(CreateRoleDTO createRoleDTO) {
        log.info("Creating new role: {}", createRoleDTO.getName());

        // Validate role name uniqueness
        if (roleRepository.existsByName(createRoleDTO.getName())) {
            throw new RuntimeException("Role with name '" + createRoleDTO.getName() + "' already exists");
        }

        Role role = modelMapper.map(createRoleDTO, Role.class);
        Role savedRole = roleRepository.save(role);

        log.info("Role created successfully with ID: {}", savedRole.getId());

        return modelMapper.map(savedRole, RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        log.debug("Fetching role by ID: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleResponseDTO getRoleResponseById(Long id) {
        log.debug("Fetching role response by ID: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        RoleResponseDTO response = modelMapper.map(role, RoleResponseDTO.class);
        response.setUserCount(role.getUsers().size());

        return response;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        log.debug("Fetching all roles");

        return roleRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponseDTO> getAllRoleResponses() {
        log.debug("Fetching all roles with user count");

        return roleRepository.findAll().stream()
                .map(role -> {
                    RoleResponseDTO dto = modelMapper.map(role, RoleResponseDTO.class);
                    dto.setUserCount(role.getUsers().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(Long id, UpdateRoleDTO updateRoleDTO) {
        log.info("Updating role with ID: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if name changed and is still unique
        if (updateRoleDTO.getName() != null &&
                !updateRoleDTO.getName().equals(role.getName()) &&
                roleRepository.existsByName(updateRoleDTO.getName())) {
            throw new RuntimeException("Role with name '" + updateRoleDTO.getName() + "' already exists");
        }

        // Update fields if provided
        if (updateRoleDTO.getName() != null) {
            role.setName(updateRoleDTO.getName());
        }
        if (updateRoleDTO.getDescription() != null) {
            role.setDescription(updateRoleDTO.getDescription());
        }
        if (updateRoleDTO.getPermissions() != null) {
            role.setPermissions(updateRoleDTO.getPermissions());
        }
        if (updateRoleDTO.getIsDefault() != null) {
            role.setIsDefault(updateRoleDTO.getIsDefault());
        }

        Role updatedRole = roleRepository.save(role);
        log.info("Role updated successfully: {}", updatedRole.getName());

        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public void deleteRole(Long id) {
        log.info("Deleting role with ID: {}", id);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if role is system role (cannot delete)
        if (role.getIsSystem()) {
            throw new RuntimeException("Cannot delete system role");
        }

        // Check if role has users
        if (!role.getUsers().isEmpty()) {
            throw new RuntimeException("Cannot delete role that has assigned users");
        }

        roleRepository.delete(role);
        log.info("Role deleted successfully: {}", role.getName());
    }

    @Override
    public RoleDTO getRoleByName(String name) {
        log.debug("Fetching role by name: {}", name);

        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));

        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> searchRoles(String keyword) {
        log.debug("Searching roles with keyword: {}", keyword);

        return roleRepository.searchRoles(keyword).stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> getDefaultRoles() {
        log.debug("Fetching default roles");

        return roleRepository.findByIsDefaultTrue().stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> getCustomRoles() {
        log.debug("Fetching custom roles");

        return roleRepository.findByIsSystemFalse().stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponseDTO> getRolesWithUserCount() {
        log.debug("Fetching roles with user count");

        return roleRepository.findAll().stream()
                .map(role -> {
                    RoleResponseDTO dto = modelMapper.map(role, RoleResponseDTO.class);
                    dto.setUserCount(role.getUsers().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO addPermission(Long roleId, String permission) {
        log.info("Adding permission '{}' to role ID: {}", permission, roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.addPermission(permission);
        Role updatedRole = roleRepository.save(role);

        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public RoleDTO removePermission(Long roleId, String permission) {
        log.info("Removing permission '{}' from role ID: {}", permission, roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.removePermission(permission);
        Role updatedRole = roleRepository.save(role);

        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public boolean hasPermission(Long roleId, String permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return role.hasPermission(permission);
    }

    @Override
    public List<String> getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
            return List.of();
        }

        return Arrays.stream(role.getPermissions().split(","))
                .map(String::trim)
                .filter(permission -> !permission.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO setAsDefault(Long roleId) {
        log.info("Setting role {} as default", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Remove default status from all other roles
        List<Role> defaultRoles = roleRepository.findByIsDefaultTrue();
        defaultRoles.forEach(r -> r.setIsDefault(false));
        roleRepository.saveAll(defaultRoles);

        // Set this role as default
        role.setIsDefault(true);
        Role updatedRole = roleRepository.save(role);

        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public RoleDTO removeAsDefault(Long roleId) {
        log.info("Removing default status from role {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setIsDefault(false);
        Role updatedRole = roleRepository.save(role);

        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public boolean roleExists(Long roleId) {
        return roleRepository.existsById(roleId);
    }

    @Override
    public boolean roleNameExists(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public RoleDTO getOrCreateDefaultUserRole() {
        return roleRepository.findByName("USER")
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .orElseGet(() -> {
                    // Create default USER role
                    CreateRoleDTO defaultRole = new CreateRoleDTO();
                    defaultRole.setName("USER");
                    defaultRole.setDescription("Default user role");
                    defaultRole.setPermissions("USER_READ,USER_UPDATE");
                    defaultRole.setIsDefault(true);

                    return createRole(defaultRole);
                });
    }

    @Override
    public void initializeDefaultRoles() {
        log.info("Initializing default roles");

        // Create default roles if they don't exist
        String[][] defaultRoles = {
                {"ADMIN", "System Administrator", "ALL", "true", "true"},
                {"USER", "Regular User", "USER_READ,USER_UPDATE", "true", "false"},
                {"MODERATOR", "Content Moderator", "USER_READ,CONTENT_MANAGE", "false", "false"},
                {"STUDENT", "Student", "STUDENT_READ,STUDENT_UPDATE", "false", "false"},
                {"TEACHER", "Teacher", "TEACHER_READ,TEACHER_UPDATE,STUDENT_READ", "false", "false"}
        };

        for (String[] roleData : defaultRoles) {
            if (!roleRepository.existsByName(roleData[0])) {
                Role role = new Role();
                role.setName(roleData[0]);
                role.setDescription(roleData[1]);
                role.setPermissions(roleData[2]);
                role.setIsDefault(Boolean.parseBoolean(roleData[3]));
                role.setIsSystem(Boolean.parseBoolean(roleData[4]));

                roleRepository.save(role);
                log.info("Created default role: {}", roleData[0]);
            }
        }
    }
}