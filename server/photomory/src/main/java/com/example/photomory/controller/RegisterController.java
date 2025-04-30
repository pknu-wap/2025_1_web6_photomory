package com.example.photomory.controller;

import com.example.photomory.dto.RegisterRequestDto;
import com.example.photomory.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    private final RegisterService userService;

    // 생성자 주입: Spring이 @Service 붙은 RegisterService를 자동 주입함
    public RegisterController(RegisterService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequestDto userRequestDto) {
        String message = userService.register(userRequestDto);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
