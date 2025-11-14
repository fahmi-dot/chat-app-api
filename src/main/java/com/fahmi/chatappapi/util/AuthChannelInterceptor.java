package com.fahmi.chatappapi.util;

import com.fahmi.chatappapi.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.verifyToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    accessor.setUser(new UsernamePasswordAuthenticationToken(username, null, null));
                } else {
                    throw new CustomException.AuthenticationException("Invalid JWT token in WebSocket connection.");
                }
            } else {
                throw new CustomException.AuthenticationException("Missing Authorization header in STOMP CONNECT.");
            }
        }

        return message;
    }
}

