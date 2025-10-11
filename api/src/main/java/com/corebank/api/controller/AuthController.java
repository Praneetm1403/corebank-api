package com.corebank.api.controller;

import com.corebank.api.model.User;
import com.corebank.api.repository.UserRepository;
import com.corebank.api.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.username())
                .filter(user -> request.password().equals(user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername(), Map.of(
                            "role", user.getRole().name()
                    ));
                    return ResponseEntity.ok(Map.of("token", token, "role", user.getRole().name()));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

}
