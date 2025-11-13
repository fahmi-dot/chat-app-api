package com.fahmi.chatappapi.mapper;

import com.fahmi.chatappapi.dto.request.UserRegisterRequest;
import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.entity.User;

import java.time.LocalDateTime;

public class UserMapper {
    public static User fromRegisterRequest(UserRegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .displayName(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .avatarUrl("url")
                .password(request.getPassword())
                .role("USER")
                .isVerified(false)
                .codeExpiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .build();
    }

    public static UserSearchResponse toSearchResponse(User user) {
        return UserSearchResponse.builder()
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
