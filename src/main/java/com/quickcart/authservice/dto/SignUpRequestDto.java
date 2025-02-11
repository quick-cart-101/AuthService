package com.example.quickcart.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String fullName;
    private String contactNumber;
    private String password;
    private String email;
    private String address;
}
