package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.config.AppConfig;
import com.fahmi.chatappapi.dto.request.TokenRequest;
import com.fahmi.chatappapi.dto.request.UserLoginRequest;
import com.fahmi.chatappapi.dto.request.UserRegisterRequest;
import com.fahmi.chatappapi.dto.request.UserVerifyRequest;
import com.fahmi.chatappapi.dto.response.TokenResponse;
import com.fahmi.chatappapi.dto.response.UserLoginResponse;
import com.fahmi.chatappapi.entity.User;
import com.fahmi.chatappapi.exception.CustomException;
import com.fahmi.chatappapi.mapper.UserMapper;
import com.fahmi.chatappapi.repository.UserRepository;
import com.fahmi.chatappapi.service.AuthService;
import com.fahmi.chatappapi.service.EmailService;
import com.fahmi.chatappapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppConfig appConfig;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(UserRegisterRequest request) {
        String code = String.format("%04d", new Random().nextInt(9999));
        User user;

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new CustomException.ResourceNotFoundException("Phone number is not found."));
            if (user.isVerified()) {
                throw new CustomException.ConflictException("Phone number is already registered.");
            }
            user.setEmail(request.getEmail());
        } else {
            user = UserMapper.fromRegisterRequest(request);
        }

        long number = userRepository.getUsernameNumber();

        user.setUsername("user" + number);
        user.setDisplayName("user" + number);
        user.setAvatarUrl(appConfig.getDefaultAvatarUrl());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationCode(code);
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        emailService.sendVerificationCode(user.getEmail(), code);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new CustomException.BadRequestException("Username and password is required.");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException.AuthenticationException("Email or password is incorrect.");
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
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("User not found."));

        if (user.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException.ConflictException("Verification code has expired.");
        }

        if (!user.getVerificationCode().equals(request.getVerificationCode())) {
            throw new CustomException.AuthenticationException("Verification code is incorrect.");
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
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("User not found."));
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
