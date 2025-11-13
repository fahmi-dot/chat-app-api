package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.TokenRequest;
import com.fahmi.chatappapi.dto.request.UserLoginRequest;
import com.fahmi.chatappapi.dto.request.UserRegisterRequest;
import com.fahmi.chatappapi.dto.request.UserVerifyRequest;
import com.fahmi.chatappapi.dto.response.TokenResponse;
import com.fahmi.chatappapi.dto.response.UserLoginResponse;

public interface AuthService {
    void register(UserRegisterRequest request);

    UserLoginResponse login(UserLoginRequest request);

    void verify(UserVerifyRequest request);

    TokenResponse refreshToken(TokenRequest request);
}
