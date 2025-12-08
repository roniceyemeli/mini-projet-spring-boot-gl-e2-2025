package com.service.user.service;

import com.service.user.dto.auth.*;
import com.service.user.dto.user.CreateUserDTO;
import com.service.user.dto.user.UserDTO;
import com.service.user.entity.User;
import com.service.user.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AuthService implements IAuthService {

    private final IServiceUser userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Authenticate user
        UserDTO userDTO = userService.authenticateUser(request.getEmail(), request.getPassword());

        if (userDTO == null) {
            throw new RuntimeException("Invalid credentials");
        }

        // Update last login
        userService.updateLastLogin(userDTO.getId());

        // Generate token
        String token = jwtUtil.generateToken(
                userDTO.getEmail(),
                userDTO.getId(),
                userDTO.getRole() != null ? userDTO.getRole().getName() : "USER"
        );

        // Create response
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUserId(userDTO.getId());
        response.setEmail(userDTO.getEmail());
        response.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
        response.setRole(userDTO.getRole() != null ? userDTO.getRole().getName() : "USER");
        response.setRoleName(response.getRole());
        response.setRoleId(userDTO.getRole() != null ? userDTO.getRole().getId() : null);
        response.setSchoolId(userDTO.getSchoolId());
        response.setExpiresAt(LocalDateTime.now().plusHours(24)); // 24 hours expiration

        log.info("Login successful for user ID: {}", userDTO.getId());

        return response;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        log.info("Registration attempt for email: {}", request.getEmail());

        // Create user DTO from request
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail(request.getEmail());
        createUserDTO.setPassword(request.getPassword());
        createUserDTO.setFirstName(request.getFirstName());
        createUserDTO.setLastName(request.getLastName());
        createUserDTO.setPhone(request.getPhone());
        createUserDTO.setRoleId(request.getRoleId());
        createUserDTO.setSchoolId(request.getSchoolId());

        // Create user
        UserDTO createdUser = userService.createUser(createUserDTO);

        // Generate token
        String token = jwtUtil.generateToken(
                createdUser.getEmail(),
                createdUser.getId(),
                createdUser.getRole() != null ? createdUser.getRole().getName() : "USER"
        );

        // Create response
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUserId(createdUser.getId());
        response.setEmail(createdUser.getEmail());
        response.setFullName(createdUser.getFirstName() + " " + createdUser.getLastName());
        response.setRole(createdUser.getRole() != null ? createdUser.getRole().getName() : "USER");
        response.setRoleName(response.getRole());
        response.setRoleId(createdUser.getRole() != null ? createdUser.getRole().getId() : null);
        response.setSchoolId(createdUser.getSchoolId());
        response.setExpiresAt(LocalDateTime.now().plusHours(24));

        log.info("Registration successful for user ID: {}", createdUser.getId());

        // TODO: Send verification email

        return response;
    }

    @Override
    public void logout(String token) {
        // In a stateless JWT system, we typically just invalidate the token on client side
        // For server-side invalidation, you could add the token to a blacklist
        log.info("Logout for token: {}", token.substring(0, Math.min(20, token.length())) + "...");
    }

    @Override
    public void requestPasswordReset(PasswordResetRequestDTO request) {
        log.info("Password reset request for email: {}", request.getEmail());

        UserDTO userDTO = userService.getUserByEmailForAuth(request.getEmail());
        if (userDTO == null) {
            // Don't reveal that user doesn't exist for security
            return;
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();

        // TODO: Update user with reset token in database
        // TODO: Send password reset email with token

        log.info("Password reset token generated for user ID: {}", userDTO.getId());
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirmDTO request) {
        log.info("Password reset confirmation with token");

        // TODO: Validate token and update password
        // 1. Find user by reset token
        // 2. Check token expiration
        // 3. Update password
        // 4. Clear reset token

        log.info("Password reset completed");
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public String refreshToken(String oldToken) {
        if (!jwtUtil.validateToken(oldToken)) {
            throw new RuntimeException("Invalid token");
        }

        String username = jwtUtil.extractUsername(oldToken);
        Long userId = jwtUtil.extractUserId(oldToken);
        String role = jwtUtil.extractRole(oldToken);

        return jwtUtil.generateToken(username, userId, role);
    }

    @Override
    public void sendVerificationEmail(String email) {
        log.info("Sending verification email to: {}", email);

        UserDTO userDTO = userService.getUserByEmailForAuth(email);
        if (userDTO == null || userDTO.getIsVerified()) {
            return;
        }

        String verificationToken = UUID.randomUUID().toString();

        // TODO: Update user with verification token
        // TODO: Send verification email

        log.info("Verification email sent to user ID: {}", userDTO.getId());
    }

    @Override
    public void verifyEmail(String token) {
        log.info("Email verification with token");

        // TODO: Validate token and verify user
        // 1. Find user by verification token
        // 2. Check token expiration
        // 3. Mark user as verified
        // 4. Clear verification token

        log.info("Email verification completed");
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return jwtUtil.extractUserId(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    @Override
    public String getRoleFromToken(String token) {
        return jwtUtil.extractRole(token);
    }
}