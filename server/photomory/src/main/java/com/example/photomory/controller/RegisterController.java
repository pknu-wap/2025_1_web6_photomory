package com.example.photomory.controller;

import com.example.photomory.dto.UserRequestDto;
import com.example.photomory.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) { //spring이 자동으로 userService를 넣어 @Service로 인해서.
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRequestDto userRequestDto) {
        String message = userService.register(userRequestDto); // new로 하면
        return message;
    }
}
