package com.service.user.controller;

import com.service.user.dto.user.*;
import com.service.user.service.IServiceUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRestController {

    private final IServiceUser userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(createUserDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponseById(id));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == #email")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/minimal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserMinimalDTO>> getAllMinimalUsers() {
        return ResponseEntity.ok(userService.getAllMinimalUsers());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(userService.getUsersByRole(roleId));
    }

    @GetMapping("/school/{schoolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersBySchool(@PathVariable Long schoolId) {
        return ResponseEntity.ok(userService.getUsersBySchool(schoolId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> verifyUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.verifyUser(id));
    }

    @GetMapping("/stats/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(userService.getTotalUsersCount());
    }

    @GetMapping("/stats/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getActiveUsersCount() {
        return ResponseEntity.ok(userService.getActiveUsersCount());
    }
}