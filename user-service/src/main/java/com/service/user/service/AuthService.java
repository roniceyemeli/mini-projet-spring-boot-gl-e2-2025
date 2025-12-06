package com.service.user.service;

import com.service.user.dto.user.UserDTO;
import com.service.user.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private ServiceUser userServiceClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        // Communication synchrone avec user-service
        UserDTO user = userServiceClient.getUserByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), "USER");

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole("USER");

        return response;
    }

    public AuthResponse register(RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(passwordEncoder.encode(request.getPassword()));
        userDTO.setFirstName(request.getFirstName());
        userDTO.setLastName(request.getLastName());
        userDTO.setRoleId(request.getRoleId());
        userDTO.setSchoolId(request.getSchoolId());

        UserDTO createdUser = userServiceClient.createUser(userDTO);

        String token = jwtUtil.generateToken(createdUser.getEmail(), createdUser.getId(), "USER");

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(createdUser.getId());
        response.setEmail(createdUser.getEmail());
        response.setRole("USER");

        return response;
    }
}