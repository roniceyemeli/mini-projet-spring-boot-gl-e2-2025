package com.service.user.service;

import com.service.user.dto.auth.*;

public interface IAuthService {

    AuthResponseDTO login(LoginRequestDTO request);
    AuthResponseDTO register(RegisterRequestDTO request);
    void logout(String token);

    // Password Management
    void requestPasswordReset(PasswordResetRequestDTO request);
    void confirmPasswordReset(PasswordResetConfirmDTO request);

    // Token Validation
    boolean validateToken(String token);
    String refreshToken(String oldToken);

    // Verification
    void sendVerificationEmail(String email);
    void verifyEmail(String token);

    // Utility
    Long getUserIdFromToken(String token);
    String getUsernameFromToken(String token);
    String getRoleFromToken(String token);
}