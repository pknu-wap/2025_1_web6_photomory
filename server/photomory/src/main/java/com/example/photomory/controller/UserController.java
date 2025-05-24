package com.example.photomory.controller;

import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.dto.UserProfileUpdateRequest;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserProfileUpdateRequest dto
    ) {
        userService.updateUserProfile(userDetails.getUser(), dto);
        return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
    }
}
