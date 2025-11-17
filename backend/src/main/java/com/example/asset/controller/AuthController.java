package com.example.asset.controller;

import com.example.asset.entity.User;
import com.example.asset.repository.UserRepository;
import com.example.asset.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        return userRepo.findByUsername(username)
            .map(user -> {
                if (BCrypt.checkpw(password, user.getPasswordHash())) {
                    String token = jwtUtil.generateToken(user);
                    return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername()));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
                }
            }).orElseGet(() -> ResponseEntity.status(401).body(Map.of("error","Invalid credentials")));
    }
}
