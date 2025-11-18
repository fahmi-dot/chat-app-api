package com.fahmi.chatappapi.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenHolder {
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    public String getId() {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        return jwtUtil.extractId(token);
    }
}
