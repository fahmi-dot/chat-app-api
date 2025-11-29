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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenHolder tokenHolder;

    @Override
    public UserResponse getMyProfile() {
        String id = tokenHolder.getId();
        User user = findById(id);

        return UserMapper.toResponse(user);
    }

    @Override
    public UserSearchResponse getUserProfile(String username) {
        User user = findByUsername(username);

        return UserMapper.toSearchResponse(user);
    }

    @Override
    public List<UserSearchResponse> searchUser(String key) {
        List<User> users = userRepository.findByUsernameIsContainingIgnoreCase(key);

        if (users.isEmpty()) return null;

        return users.stream().map(UserMapper::toSearchResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse updateProfile(String id, UserUpdateRequest request) {
        User user = findById(id);

        if (!request.getDisplayName().equals(user.getDisplayName())) {
            user.setDisplayName(request.getDisplayName());
        }

        if (!request.getUsername().isEmpty() &&
                !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new CustomException.ResourceNotFoundException("Username already exists.");
            }

            user.setUsername(request.getUsername());
        }

        if (!request.getBio().equals(user.getBio())) {
            user.setBio(request.getBio());
        }

        return UserMapper.toResponse(userRepository.save(user));
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
