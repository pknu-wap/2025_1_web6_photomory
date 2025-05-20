package com.example.photomory.controller;

import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(userService.getUserProfile(user));
    }
}
