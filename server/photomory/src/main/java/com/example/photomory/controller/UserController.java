package com.example.photomory.controller;

import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
