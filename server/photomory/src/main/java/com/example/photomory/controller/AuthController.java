package com.example.photomory.controller;

import com.example.photomory.dto.UserRequestDto;
import com.example.photomory.dto.UserResponseDto;
import com.example.photomory.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class AuthController {
    @PostMapping("/register")
    public String register(@RequestBody UserRequestDto userRequestDto) {
        UserService userService = new UserService();
        String result = userService.register(userRequestDto);
        return result;

    }
}
