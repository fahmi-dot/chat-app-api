package com.fahmi.chatappapi.controller;

import com.fahmi.chatappapi.constant.Endpoint;
import com.fahmi.chatappapi.dto.response.UserResponse;
import com.fahmi.chatappapi.dto.response.UserSearchResponse;
import com.fahmi.chatappapi.service.UserService;
import com.fahmi.chatappapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getMyProfile() {
        UserResponse response = userService.getMyProfile();

        return ResponseUtil.response(HttpStatus.OK, "My profile retrieved successfully.", response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam String key) {
        UserSearchResponse response = userService.searchUser(key);

        return ResponseUtil.response(HttpStatus.OK, "User found.", response);
    }
}
