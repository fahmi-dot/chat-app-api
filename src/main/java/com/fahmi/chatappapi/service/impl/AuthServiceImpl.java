package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.dto.request.TokenRequest;
import com.fahmi.chatappapi.dto.request.UserLoginRequest;
import com.fahmi.chatappapi.dto.request.UserRegisterRequest;
import com.fahmi.chatappapi.dto.request.UserVerifyRequest;
import com.fahmi.chatappapi.dto.response.TokenResponse;
import com.fahmi.chatappapi.dto.response.UserLoginResponse;
import com.fahmi.chatappapi.entity.User;
import com.fahmi.chatappapi.mapper.UserMapper;
import com.fahmi.chatappapi.repository.UserRepository;
import com.fahmi.chatappapi.service.AuthService;
import com.fahmi.chatappapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already registered.");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        User user = UserMapper.fromRegisterRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationCode(code);
        userRepository.save(user);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Username and password is required.");
        }
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email or password is incorrect.");
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return UserLoginResponse.builder()
                .user(UserMapper.toResponse(user))
                .tokens(tokenResponse)
                .build();
    }

    @Override
    public void verify(UserVerifyRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code expired.");
        }
        if (!user.getVerificationCode().equals(request.getVerificationCode())) {
            throw new RuntimeException("Verification code is incorrect.");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        userRepository.save(user);
    }

    @Override
    public TokenResponse refreshToken(TokenRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
