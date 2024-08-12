package com.valr.api.controller.auth;

import com.valr.api.dto.auth.AuthRequestDTO;
import com.valr.api.dto.auth.AuthResponseDTO;
import com.valr.api.dto.auth.RefreshTokenRequestDTO;
import com.valr.api.dto.auth.RefreshTokenResponseDTO;
import com.valr.api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Authentication", description = "The controller for authenticating users")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(description = "Login users with the email and password seeded. A token is returned; supply that in the"
            + " header of subsequent requests as a Bearer Token(Oauth2.0)")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponseDTO authenticate(@Parameter(description = "Authentication Request", required = true)
                                            @RequestBody AuthRequestDTO authRequest) {
        return authenticationService.authentication(authRequest);
    }

    @Operation(description = "Refesh token generated from login to stay signed in")
    @PostMapping("/refresh")
    public RefreshTokenResponseDTO refreshAccessToken(@Parameter(description = "Refresh Token Request", required = true)
                                                          @RequestBody RefreshTokenRequestDTO request) {
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
