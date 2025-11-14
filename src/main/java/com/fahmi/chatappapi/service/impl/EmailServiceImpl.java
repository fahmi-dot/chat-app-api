package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String receiverEmail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(receiverEmail);
            helper.setSubject("Hello! account verification code");
            helper.setFrom(senderEmail, "Hello!");

            String body = """
                <h1>Hello!</h1>
                <p>Here is your <b>Hello!</b> account verification code: <b>%s<b/></p><br/>
                <p>Thanks!</p>
                <p>The Hello! team</p>
                """.formatted(verificationCode);

            helper.setText(body, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
