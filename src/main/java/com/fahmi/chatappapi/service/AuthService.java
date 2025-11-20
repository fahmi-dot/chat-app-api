package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.*;
import com.fahmi.chatappapi.dto.response.TokenResponse;
import com.fahmi.chatappapi.dto.response.UserLoginResponse;
import com.fahmi.chatappapi.dto.response.UserResponse;

public interface AuthService {
    UserLoginResponse login(UserLoginRequest request);

    void forgotPassword(UserForgotRequest request);

    void register(UserRegisterRequest request);

    void resendCode(ResendCodeRequest request);

    UserResponse verify(UserVerifyRequest request);

    TokenResponse refreshToken(TokenRequest request);
}
