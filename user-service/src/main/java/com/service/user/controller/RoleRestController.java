package com.service.user.controller;

import com.service.school.dto.RoleDTO;
import com.service.user.entity.Role;
import com.service.user.service.ServiceRole;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class RoleRestController {

    private final ServiceRole roleService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role createdRole = roleService.createRole(role);
        RoleDTO responseDTO = modelMapper.map(createdRole, RoleDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id) {
        Role role = roleService.getRoleById(id);
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleDTO> roleDTOs = roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roleDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoleDTO>> searchRoles(@RequestParam String keyword) {
        List<Role> roles = roleService.searchRoles(keyword);
        List<RoleDTO> roleDTOs = roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roleDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Integer id,
                                              @Valid @RequestBody RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role updatedRole = roleService.updateRole(id, role);
        RoleDTO responseDTO = modelMapper.map(updatedRole, RoleDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<RoleDTO> addPermission(@PathVariable Integer id,
                                                 @RequestParam String permission) {
        Role updatedRole = roleService.addPermission(id, permission);
        RoleDTO roleDTO = modelMapper.map(updatedRole, RoleDTO.class);
        return ResponseEntity.ok(roleDTO);
    }

    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<RoleDTO> removePermission(@PathVariable Integer id,
                                                    @RequestParam String permission) {
        Role updatedRole = roleService.removePermission(id, permission);
        RoleDTO roleDTO = modelMapper.map(updatedRole, RoleDTO.class);
        return ResponseEntity.ok(roleDTO);
    }
}