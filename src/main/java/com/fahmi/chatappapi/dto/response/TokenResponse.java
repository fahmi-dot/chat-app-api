package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiryAt;
}
