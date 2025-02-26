package com.quickcart.authservice.controllers;

import com.quickcart.authservice.dto.LoginRequestDto;
import com.quickcart.authservice.dto.SessionDto;
import com.quickcart.authservice.dto.SignUpRequestDto;
import com.quickcart.authservice.exceptions.InvalidUserDetailsException;
import com.quickcart.authservice.exceptions.UserNotRegisteredException;
import com.quickcart.authservice.entities.User;
import com.quickcart.authservice.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() throws UserNotRegisteredException {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        User user = new User();
        when(authService.signUp(any(SignUpRequestDto.class))).thenReturn(user);

        ResponseEntity<User> response = authController.signUp(signUpRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testLoginUser() throws UserNotRegisteredException, InvalidUserDetailsException {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        SessionDto sessionDto = new SessionDto();
        when(authService.authenticateUser(any(LoginRequestDto.class))).thenReturn(sessionDto);

        ResponseEntity<SessionDto> response = authController.loginUser(loginRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void testLogoutUser() {
        String token = "Bearer token";
        doNothing().when(authService).logoutUser(anyString());

        ResponseEntity<String> response = authController.logoutUser(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());
    }

    @Test
    void testGetCurrentUser() {
        String token = "Bearer token";
        User user = new User();
        when(authService.getUserFromToken(anyString())).thenReturn(user);

        ResponseEntity<User> response = authController.getCurrentUser(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testRefreshToken() {
        String refreshToken = "refreshToken";
        SessionDto sessionDto = new SessionDto();
        when(authService.refreshToken(anyString())).thenReturn(sessionDto);

        ResponseEntity<SessionDto> response = authController.refreshToken(refreshToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(new User());
        when(authService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = authController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        doNothing().when(authService).deleteUser(any(UUID.class));

        ResponseEntity<String> response = authController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
    }
}