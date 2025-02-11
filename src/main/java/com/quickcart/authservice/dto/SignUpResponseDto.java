package com.example.quickcart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SignUpResponseDto {
    private UUID id;
    private String fullName;
    private String contactNumber;
    private String email;
    private String address;
}
