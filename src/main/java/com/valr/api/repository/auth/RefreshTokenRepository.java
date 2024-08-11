package com.valr.api.repository.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RefreshTokenRepository {

    private final Map<String, UserDetails> tokens = new HashMap<>();

    public UserDetails findUserDetailsByToken(String token) {
        return tokens.get(token);
    }

    public void save(String token, UserDetails userDetails) {
        tokens.put(token, userDetails);
    }
}