package com.fahmi.chatappapi.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String displayName;
    private String bio;
}
