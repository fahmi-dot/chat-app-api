package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String receiverEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiverEmail);
        message.setSubject("Verification Code");
        message.setText("Your Hello! verification code: " + verificationCode);
        mailSender.send(message);
    }
}
