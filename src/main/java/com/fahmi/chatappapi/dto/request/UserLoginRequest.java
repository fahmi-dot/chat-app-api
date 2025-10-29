package com.fahmi.chatappapi.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}