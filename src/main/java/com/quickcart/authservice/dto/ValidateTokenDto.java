package com.quickcart.authservice.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenDto {
    private String token;
    private Long userId;
}
