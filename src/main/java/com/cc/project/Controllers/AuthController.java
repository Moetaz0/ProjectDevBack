package com.cc.project.Controllers;

import com.cc.project.Entity.User;
import com.cc.project.Repository.UserRepository;
import com.cc.project.Security.JwtUtil;
import com.cc.project.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor

@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")

public class AuthController {
    private final Map<String, String> resetCodes = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid password"));
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "userId", user.getId()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User newUser) {
        try {
            // Validate required fields
            if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
            }
            if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }
            if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
            }

            if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username already taken"));
            }
            if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already taken"));
            }

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setRole(User.Role.CLIENT); // default role
            userRepository.save(newUser);

            // Send welcome email
            try {
                emailService.sendWelcomeEmail(newUser.getEmail(), newUser.getUsername());
            } catch (Exception e) {
                System.err.println("Failed to send welcome email: " + e.getMessage());
            }

            String token = jwtUtil.generateToken(newUser.getEmail(), newUser.getRole().name(), newUser.getId());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", newUser.getId(),
                    "message", "User registered successfully",
                    "username", newUser.getUsername(),
                    "email", newUser.getEmail(),
                    "role", newUser.getRole().name()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Registration failed", "details", e.getMessage()));
        }
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/verifycode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (email == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and code are required"));
        }

        String storedCode = resetCodes.get(email); // Map<String, String> resetCodes
        if (storedCode == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No code found for this email"));
        }

        if (!storedCode.equals(code)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid code"));
        }

        return ResponseEntity.ok(Map.of("message", "Code verified successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email not found"));
        }

        User user = userOpt.get();

        // Generate 6-digit reset code
        String code = String.format("%06d", new Random().nextInt(999999));
        resetCodes.put(email, code);

        // Send templated email
        try {
            emailService.sendPasswordResetEmail(email, code, user.getUsername());
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Failed to send reset code"));
        }

        return ResponseEntity.ok(Map.of("message", "Reset code sent to email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");

        if (!resetCodes.containsKey(email) || !resetCodes.get(email).equals(code)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired code"));
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Remove code after successful reset
        resetCodes.remove(email);

        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}