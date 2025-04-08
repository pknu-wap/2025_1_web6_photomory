package com.example.photomory.controller;

import com.example.photomory.dto.UserRequestDto;
import com.example.photomory.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class AuthController {
    @PostMapping("/register")
    public String register(@RequestBody UserRequestDto userRequestDto) {
        UserService userService = new UserService();
        String message = userService.register(userRequestDto);
        return message;
    }
}
