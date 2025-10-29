package com.fahmi.chatappapi.service;

import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.entity.User;

public interface UserService {
    UserResponse getMyProfile();

    UserSearchResponse searchUser(String key);

    User findByUsername(String username);
}
