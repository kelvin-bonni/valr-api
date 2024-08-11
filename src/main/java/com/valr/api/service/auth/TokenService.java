package com.valr.api.service.auth;

import com.valr.api.security.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class TokenService {

    private final SecretKey secretKey;

    public TokenService(JWTProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getKey().getBytes());
    }

    public String generate(UserDetails userDetails, Date expirationDate) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String generate(UserDetails userDetails, Date expirationDate, Map<String, Object> additionalClaims) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .add(additionalClaims)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return userDetails.getUsername().equals(email) && !isExpired(token);
    }

    public String extractEmail(String token) {
        return getAllClaims(token).getSubject();
    }

    public boolean isExpired(String token) {
        return getAllClaims(token)
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}