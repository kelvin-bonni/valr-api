package com.valr.api.dto.auth;

import lombok.Getter;

@Getter
public class AuthRequestDTO {
    private String email;
    private String password;
}
