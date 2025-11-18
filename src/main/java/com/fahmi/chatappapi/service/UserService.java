package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.request.UserUpdateRequest;
import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.entity.User;

public interface UserService {
    UserResponse getMyProfile();

    UserSearchResponse searchUser(String key);

    void updateProfile(String id, UserUpdateRequest request);

    User findById(String id);

    User findByUsername(String username);
}
