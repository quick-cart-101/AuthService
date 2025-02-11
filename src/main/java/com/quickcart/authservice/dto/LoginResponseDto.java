package com.example.quickcart.dto;

import com.example.quickcart.models.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class LoginResponseDto {
    private UUID id;
    private String email;
    private List<Role> roles = new ArrayList<>();
}
