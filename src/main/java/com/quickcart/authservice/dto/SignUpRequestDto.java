package com.quickcart.authservice.dto;

import com.quickcart.authservice.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String address;
    private Set<Role> roles;
}
