package com.quickcart.authservice.services.impl;

import com.quickcart.authservice.dto.LoginRequestDto;
import com.quickcart.authservice.dto.SessionDto;
import com.quickcart.authservice.dto.SignUpRequestDto;
import com.quickcart.authservice.exceptions.InvalidUserDetailsException;
import com.quickcart.authservice.exceptions.UserNotRegisteredException;
import com.quickcart.authservice.models.Session;
import com.quickcart.authservice.models.Status;
import com.quickcart.authservice.models.User;
import com.quickcart.authservice.repositories.SessionRepository;
import com.quickcart.authservice.repositories.UserRepository;
import com.quickcart.authservice.services.AuthService;
import com.quickcart.authservice.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User signUp(SignUpRequestDto signUpRequestDto) throws UserNotRegisteredException {
        if (userRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            log.error("User is not found with email: {}", signUpRequestDto.getEmail());
            throw new UserNotRegisteredException("Email is already in use");
        }

        User user = new User();
        user.setName(signUpRequestDto.getName());
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setContactNumber(signUpRequestDto.getContactNumber());
        user.setAddress(signUpRequestDto.getAddress());
        user.setRoles(signUpRequestDto.getRoles());

        return userRepository.save(user);
    }

    @Override
    public SessionDto authenticateUser(LoginRequestDto loginRequestDto) throws UserNotRegisteredException, InvalidUserDetailsException {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotRegisteredException("User not registered with email: " + loginRequestDto.getEmail()));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            log.info("Password is invalid for user with email: {}", user.getEmail());
            throw new InvalidUserDetailsException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());
        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setStatus(Status.ACTIVE);
        sessionRepository.save(session);
        log.debug("session: {} is created and saved", session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setToken(token);
        sessionDto.setStatus(Status.ACTIVE);

        return sessionDto;
    }

    @Override
    @Transactional
    public void logoutUser(String token) {
        Optional<Session> session = sessionRepository.findByToken(token);
        session.ifPresent(s -> {
            s.setStatus(Status.INACTIVE);
            log.info("User with email: {} is successfully logged out.", s.getUser().getEmail());
            sessionRepository.save(s);
        });
    }

    @Override
    public User getUserFromToken(String token) {
        if(token.startsWith("Bearer")) {
            token = token.replace("Bearer ", StringUtils.EMPTY);
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public SessionDto refreshToken(String refreshToken) {
        Optional<Session> session = sessionRepository.findByToken(refreshToken);

        if (session.isEmpty() || session.get().getStatus() != Status.ACTIVE) {
            log.info("Refresh token: {} is expired or invalid", refreshToken);
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String newToken = jwtTokenProvider.generateToken(session.get().getUser().getEmail());

        Session newSession = new Session();
        newSession.setUser(session.get().getUser());
        newSession.setToken(newToken);
        newSession.setStatus(Status.ACTIVE);
        log.info("New Token is generated using refresh token for user with email: {}", newSession.getUser().getEmail());
        sessionRepository.save(newSession);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setToken(newToken);
        sessionDto.setStatus(Status.ACTIVE);

        return sessionDto;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
