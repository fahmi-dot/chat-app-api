package com.fahmi.chatappapi.dto.request;

import lombok.Data;

@Data
public class UserVerifyRequest {
    private String phoneNumber;
    private String verificationCode;
}
