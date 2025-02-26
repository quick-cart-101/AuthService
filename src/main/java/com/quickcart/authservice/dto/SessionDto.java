package com.quickcart.authservice.dto;

import com.quickcart.authservice.entities.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionDto {
    private String token;
    private Status status;
}
