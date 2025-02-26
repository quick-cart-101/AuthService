package com.quickcart.authservice.services;

import com.quickcart.authservice.dto.LoginRequestDto;
import com.quickcart.authservice.dto.SessionDto;
import com.quickcart.authservice.dto.SignUpRequestDto;
import com.quickcart.authservice.exceptions.InvalidUserDetailsException;
import com.quickcart.authservice.exceptions.UserNotRegisteredException;
import com.quickcart.authservice.entities.Role;
import com.quickcart.authservice.entities.Session;
import com.quickcart.authservice.entities.Status;
import com.quickcart.authservice.entities.User;
import com.quickcart.authservice.repositories.SessionRepository;
import com.quickcart.authservice.repositories.UserRepository;
import com.quickcart.authservice.services.impl.AuthServiceImpl;
import com.quickcart.authservice.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private SignUpRequestDto signUpRequestDto;
    private LoginRequestDto loginRequestDto;
    private Session session;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.setContactNumber("1234567890");
        user.setAddress("123 Main St");

        user.setRoles(Set.of(Role.CUSTOMER));

        signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setName("John Doe");
        signUpRequestDto.setEmail("john.doe@example.com");
        signUpRequestDto.setPassword("password");
        signUpRequestDto.setContactNumber("1234567890");
        signUpRequestDto.setAddress("123 Main St");
        signUpRequestDto.setRoles(Set.of(Role.CUSTOMER));

        loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("john.doe@example.com");
        loginRequestDto.setPassword("password");

        session = new Session();
        session.setUser(user);
        session.setToken("token");
        session.setStatus(Status.ACTIVE);
    }

    @Test
    void testSignUp_Success() throws UserNotRegisteredException {
        when(userRepository.findByEmail(signUpRequestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authService.signUp(signUpRequestDto);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignUp_EmailAlreadyInUse() {
        when(userRepository.findByEmail(signUpRequestDto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserNotRegisteredException.class, () -> authService.signUp(signUpRequestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Success() throws UserNotRegisteredException, InvalidUserDetailsException {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user.getEmail())).thenReturn("token");
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        SessionDto result = authService.authenticateUser(loginRequestDto);

        assertNotNull(result);
        assertEquals("token", result.getToken());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void testAuthenticateUser_UserNotRegistered() {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotRegisteredException.class, () -> authService.authenticateUser(loginRequestDto));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidUserDetailsException.class, () -> authService.authenticateUser(loginRequestDto));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testLogoutUser_Success() {
        when(sessionRepository.findByToken("token")).thenReturn(Optional.of(session));

        authService.logoutUser("token");

        assertEquals(Status.INACTIVE, session.getStatus());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testLogoutUser_TokenNotFound() {
        when(sessionRepository.findByToken("token")).thenReturn(Optional.empty());

        authService.logoutUser("token");

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testGetUserFromToken_Success() {
        when(jwtTokenProvider.getEmailFromToken("token")).thenReturn("john.doe@example.com");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        User result = authService.getUserFromToken("Bearer token");

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void testGetUserFromToken_UserNotFound() {
        when(jwtTokenProvider.getEmailFromToken("token")).thenReturn("john.doe@example.com");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.getUserFromToken("Bearer token"));
    }

    @Test
    void testRefreshToken_Success() {
        when(sessionRepository.findByToken("refreshToken")).thenReturn(Optional.of(session));
        when(jwtTokenProvider.generateToken(user.getEmail())).thenReturn("newToken");
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        SessionDto result = authService.refreshToken("refreshToken");

        assertNotNull(result);
        assertEquals("newToken", result.getToken());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void testRefreshToken_InvalidToken() {
        when(sessionRepository.findByToken("refreshToken")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.refreshToken("refreshToken"));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = authService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        authService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}