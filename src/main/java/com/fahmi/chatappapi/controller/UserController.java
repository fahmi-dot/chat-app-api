package com.fahmi.chatappapi.controller;

import com.fahmi.chatappapi.constant.Endpoint;
import com.fahmi.chatappapi.dto.request.UserUpdateRequest;
import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoint.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getMyProfile() {
        UserResponse response = userService.getMyProfile();

        return ResponseUtil.response(HttpStatus.OK, "My profile retrieved successfully.", response);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String query) {
        UserSearchResponse response = userService.getUserProfile(query);

        return ResponseUtil.response(HttpStatus.OK, "User profile retrieved successfully.", response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam String query) {
        List<UserSearchResponse> response = userService.searchUser(query);

        return ResponseUtil.response(HttpStatus.OK, "User found.", response);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<?> updateProfile(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        userService.updateProfile(id, request);

        return ResponseUtil.response(HttpStatus.OK, "Profile updated successfully.", HttpStatus.OK);
    }
}
