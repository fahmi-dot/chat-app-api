package com.fahmi.chatappapi.service;

public interface EmailService {
    void sendVerificationCode(String receiverEmail, String verificationCode);
}
