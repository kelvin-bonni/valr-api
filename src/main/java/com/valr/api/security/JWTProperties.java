package com.valr.api.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    private String key;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
}
