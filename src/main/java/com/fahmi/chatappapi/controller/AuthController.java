package com.fahmi.chatappapi.controller;

import com.fahmi.chatappapi.constant.Endpoint;
import com.fahmi.chatappapi.dto.request.*;
import com.fahmi.chatappapi.dto.response.TokenResponse;
import com.fahmi.chatappapi.dto.response.UserLoginResponse;
import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.service.AuthService;
import com.fahmi.chatappapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        authService.register(request);

        return ResponseUtil.response(HttpStatus.CREATED, "User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = authService.login(request);

        return ResponseUtil.response(HttpStatus.OK, "User logged in successfully", response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody UserVerifyRequest request) {
        UserResponse response = authService.verify(request);

        return ResponseUtil.response(HttpStatus.OK, "User verified successfully.", response);
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendCode(@RequestBody ResendCodeRequest request) {
        authService.resendCode(request);

        return ResponseUtil.response(HttpStatus.OK, "Resend code successfully.", HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest request) {
        TokenResponse response = authService.refreshToken(request);

        return ResponseUtil.response(HttpStatus.OK, "Refresh token successfully.", response);
    }
}
