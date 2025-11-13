package com.fahmi.chatappapi.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
}
