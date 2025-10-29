package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.entity.User;
import com.fahmi.chatappapi.mapper.UserMapper;
import com.fahmi.chatappapi.repository.UserRepository;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenHolder tokenHolder;

    @Override
    public UserResponse getMyProfile() {
        String username = tokenHolder.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserSearchResponse searchUser(String key) {
        User user = userRepository.findByUsername(key)
                .orElseGet(() -> userRepository.findByPhoneNumber(key)
                        .orElseThrow(() -> new RuntimeException("User not found.")));

        return UserMapper.toSearchResponse(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}
