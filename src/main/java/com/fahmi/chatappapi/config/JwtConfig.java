package com.fahmi.chatappapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {

    private String secret;
    private long accessExpirationMs;
    private long refreshExpirationMs;
}
