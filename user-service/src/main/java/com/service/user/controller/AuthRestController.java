package com.service.user.controller;

import com.service.user.dto.auth.*;
import com.service.user.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = authService.refreshToken(token.replace("Bearer ", ""));

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(newToken);
        response.setType("Bearer");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmDTO request) {
        authService.confirmPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify/request")
    public ResponseEntity<Void> requestVerification(@RequestParam String email) {
        authService.sendVerificationEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify/confirm")
    public ResponseEntity<Void> confirmVerification(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }
}