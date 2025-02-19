package com.quickcart.authservice.controllers;

import com.quickcart.authservice.dto.LoginRequestDto;
import com.quickcart.authservice.dto.SessionDto;
import com.quickcart.authservice.dto.SignUpRequestDto;
import com.quickcart.authservice.exceptions.InvalidUserDetailsException;
import com.quickcart.authservice.exceptions.UserNotRegisteredException;
import com.quickcart.authservice.models.User;
import com.quickcart.authservice.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and management")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws UserNotRegisteredException {
        User user = authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and return a session token")
    public ResponseEntity<SessionDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) throws UserNotRegisteredException, InvalidUserDetailsException {
        SessionDto session = authService.authenticateUser(loginRequestDto);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout a user by invalidating the session token")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
        authService.logoutUser(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/current-user")
    @Operation(summary = "Get the current authenticated user")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        User user = authService.getUserFromToken(token);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/refresh", consumes = "application/x-www-form-urlencoded")
    @Operation(summary = "Refresh the session token using a refresh token")
    public ResponseEntity<SessionDto> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        SessionDto newSession = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(newSession);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user by ID (Admin only)")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        authService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}