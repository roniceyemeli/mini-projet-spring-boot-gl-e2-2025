package com.service.user.controller;

import com.service.user.dto.role.*;
import com.service.user.service.IServiceRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoleRestController {

    private final IServiceRole roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.createRole(createRoleDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleResponseById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/with-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> getAllRolesWithUserCount() {
        return ResponseEntity.ok(roleService.getAllRoleResponses());
    }

    @GetMapping("/default")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getDefaultRoles() {
        return ResponseEntity.ok(roleService.getDefaultRoles());
    }

    @GetMapping("/custom")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> getCustomRoles() {
        return ResponseEntity.ok(roleService.getCustomRoles());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDTO>> searchRoles(@RequestParam String keyword) {
        return ResponseEntity.ok(roleService.searchRoles(keyword));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        return ResponseEntity.ok(roleService.updateRole(id, updateRoleDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> addPermission(
            @PathVariable UUID id,
            @RequestParam String permission) {
        return ResponseEntity.ok(roleService.addPermission(id, permission));
    }

    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> removePermission(
            @PathVariable UUID id,
            @RequestParam String permission) {
        return ResponseEntity.ok(roleService.removePermission(id, permission));
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getRolePermissions(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRolePermissions(id));
    }

    @PostMapping("/{id}/default")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> setAsDefault(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.setAsDefault(id));
    }

    @DeleteMapping("/{id}/default")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> removeAsDefault(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.removeAsDefault(id));
    }

    @PostMapping("/initialize-defaults")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initializeDefaultRoles() {
        roleService.initializeDefaultRoles();
        return ResponseEntity.ok().build();
    }
}