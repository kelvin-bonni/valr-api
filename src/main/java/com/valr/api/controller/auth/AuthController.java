package com.valr.api.controller.auth;

import com.valr.api.dto.auth.AuthRequestDTO;
import com.valr.api.dto.auth.AuthResponseDTO;
import com.valr.api.dto.auth.RefreshTokenRequestDTO;
import com.valr.api.dto.auth.RefreshTokenResponseDTO;
import com.valr.api.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponseDTO authenticate(@RequestBody AuthRequestDTO authRequest) {
        return authenticationService.authentication(authRequest);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponseDTO refreshAccessToken(@RequestBody RefreshTokenRequestDTO request) {
        String token = authenticationService.refreshAccessToken(request.getToken());
        if (token != null) {
            return mapToTokenResponse(token);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token.");
        }
    }

    private RefreshTokenResponseDTO mapToTokenResponse(String token) {
        return new RefreshTokenResponseDTO(token);
    }
}
