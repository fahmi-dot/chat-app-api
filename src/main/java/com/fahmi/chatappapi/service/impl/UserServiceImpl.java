package com.fahmi.chatappapi.service.impl;

import com.fahmi.chatappapi.dto.request.UserUpdateRequest;
import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.entity.User;
import com.fahmi.chatappapi.exception.CustomException;
import com.fahmi.chatappapi.mapper.UserMapper;
import com.fahmi.chatappapi.repository.UserRepository;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHolder tokenHolder;

    @Override
    public UserResponse getMyProfile() {
        String id = tokenHolder.getId();
        User user = findById(id);
        return UserMapper.toResponse(user);
    }

    @Override
    public UserSearchResponse searchUser(String key) {
        User user = userRepository.findByUsername(key)
                .orElseGet(() -> userRepository.findByPhoneNumber(key)
                        .orElseThrow(() -> new CustomException.ResourceNotFoundException("User not found.")));

        return UserMapper.toSearchResponse(user);
    }

    @Override
    public void updateProfile(String id, UserUpdateRequest request) {
        User user = findById(id);

        if (request.getUsername() != null) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new CustomException.ResourceNotFoundException("Username already exists.");
            }

            user.setUsername(request.getUsername());
        }

        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("User not found."));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("User not found."));
    }
}
