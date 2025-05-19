package com.example.photomory.controller;

import com.example.photomory.dto.RegisterRequestDto;
import com.example.photomory.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // 경로: /api/auth/register
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequestDto userRequestDto) {
        String message = registerService.register(userRequestDto);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
