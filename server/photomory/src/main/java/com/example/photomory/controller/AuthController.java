package com.example.photomory.controller;

import com.example.photomory.dto.LoginRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class AuthController {

    @PostMapping("/login")
    public String Login(LoginRequestDto loginRequestDto) {
        String message = "";

        return null;
    }
}
