package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String displayName;
    private String phoneNumber;
    private String email;
    private String avatarUrl;
    private String bio;
}
