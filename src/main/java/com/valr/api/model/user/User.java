package com.valr.api.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String password;
    private Role role;
}
