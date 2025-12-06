package com.service.user.controller;

import com.service.user.dto.role.CreateRoleDTO;
import com.service.user.dto.role.RoleDTO;
import com.service.user.service.ServiceRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class RoleRestController {

    @Autowired
    private ServiceRole roleService;

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody CreateRoleDTO createRoleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.createRole(createRoleDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}