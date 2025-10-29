package com.fahmi.chatappapi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchResponse {
    private String username;
    private String displayName;
    private String avatarUrl;
    private String bio;
}
