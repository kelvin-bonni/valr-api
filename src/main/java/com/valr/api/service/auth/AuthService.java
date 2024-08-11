package com.valr.api.service.auth;

import com.valr.api.dto.auth.AuthRequestDTO;
import com.valr.api.dto.auth.AuthResponseDTO;
import com.valr.api.repository.auth.RefreshTokenRepository;
import com.valr.api.security.JWTProperties;
import com.valr.api.service.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;
    private final JWTProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthService(AuthenticationManager authManager,
                       UserDetailsServiceImpl userDetailsService,
                       TokenService tokenService,
                       JWTProperties jwtProperties,
                       RefreshTokenRepository refreshTokenRepository) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AuthResponseDTO authentication(AuthRequestDTO authenticationRequest) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);

        refreshTokenRepository.save(refreshToken, user);

        return new AuthResponseDTO(
                accessToken,
                refreshToken
        );
    }

    public String refreshAccessToken(String refreshToken) {
        String extractedEmail = tokenService.extractEmail(refreshToken);

        if (extractedEmail != null) {
            UserDetails currentUserDetails = userDetailsService.loadUserByUsername(extractedEmail);
            UserDetails refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(refreshToken);

            if (!tokenService.isExpired(refreshToken) && refreshTokenUserDetails != null
                    && refreshTokenUserDetails.getUsername().equals(currentUserDetails.getUsername())) {
                return createAccessToken(currentUserDetails);
            }
        }
        return null;
    }

    private String createAccessToken(UserDetails user) {
        return tokenService.generate(
                user,
                getAccessTokenExpiration()
        );
    }

    private String createRefreshToken(UserDetails user) {
        return tokenService.generate(
                user,
                getRefreshTokenExpiration()
        );
    }

    private Date getAccessTokenExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration());
    }

    private Date getRefreshTokenExpiration() {
        return new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration());
    }
}
