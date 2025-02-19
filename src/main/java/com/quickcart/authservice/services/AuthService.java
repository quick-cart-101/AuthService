package com.quickcart.authservice.services;

import com.quickcart.authservice.dto.LoginRequestDto;
import com.quickcart.authservice.dto.SessionDto;
import com.quickcart.authservice.dto.SignUpRequestDto;
import com.quickcart.authservice.exceptions.InvalidUserDetailsException;
import com.quickcart.authservice.exceptions.UserNotRegisteredException;
import com.quickcart.authservice.models.User;

import java.util.List;
import java.util.UUID;

public interface AuthService {

    /**
     * Signs up a new user.
     *
     * @param signUpRequestDto the sign-up request data transfer object
     * @return the newly registered user
     * @throws UserNotRegisteredException if the user could not be registered
     */
    User signUp(SignUpRequestDto signUpRequestDto) throws UserNotRegisteredException;

    /**
     * Authenticates a user and returns a session token.
     *
     * @param loginRequestDto the login request data transfer object
     * @return the session data transfer object containing the token
     * @throws UserNotRegisteredException if the user is not registered
     * @throws InvalidUserDetailsException if the user details are invalid
     */
    SessionDto authenticateUser(LoginRequestDto loginRequestDto) throws UserNotRegisteredException, InvalidUserDetailsException;

    /**
     * Logs out a user by invalidating the session token.
     *
     * @param token the session token
     */
    void logoutUser(String token);

    /**
     * Retrieves the current authenticated user from the token.
     *
     * @param token the session token
     * @return the authenticated user
     */
    User getUserFromToken(String token);

    /**
     * Refreshes the session token using a refresh token.
     *
     * @param refreshToken the refresh token
     * @return the new session data transfer object containing the new token
     */
    SessionDto refreshToken(String refreshToken);

    /**
     * Retrieves all users.
     *
     * @return the list of all users
     */
    List<User> getAllUsers();

    /**
     * Deletes a user by their ID.
     *
     * @param userId the user ID
     */
    void deleteUser(UUID userId);
}