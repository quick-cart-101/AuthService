package com.example.quickcart.services;

import com.example.quickcart.dto.SignUpRequestDto;
import com.example.quickcart.dto.SignUpResponseDto;
import com.example.quickcart.models.User;
import org.antlr.v4.runtime.misc.Pair;

import java.util.UUID;

public interface AuthService {

    SignUpResponseDto signUp(SignUpRequestDto signUpRequest);

    Pair<User,String> login(String email, String password);

    Boolean validate(String token, UUID userId);
}
