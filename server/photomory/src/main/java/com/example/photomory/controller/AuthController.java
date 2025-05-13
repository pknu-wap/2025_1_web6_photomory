package com.example.photomory.controller;

import com.example.photomory.dto.LoginRequestDto;
import com.example.photomory.dto.TokenResponseDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin; // 추가 (develop-BE)
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*; // main


@CrossOrigin(origins = "https://photomory.o-r.kr", methods = {org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.OPTIONS}, allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            System.out.println("🚀 [AuthController] 로그인 요청 도착");
            System.out.println("📧 입력된 이메일: " + loginRequestDto.getUseremail());
            System.out.println("🔑 입력된 비밀번호: " + loginRequestDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUseremail(),
                            loginRequestDto.getPassword()
                    )
            );

            System.out.println("✅ [AuthController] 인증 성공: " + authentication.getName());

            UserEntity user = (UserEntity) authentication.getPrincipal();
            String userName = user.getUserName();
            String userEmail = user.getUserEmail();

            String accessToken = jwtTokenProvider.generateAccessToken(userEmail);
            String refreshToken = jwtTokenProvider.generateRefreshToken(userEmail);

            TokenResponseDto tokenResponse = new TokenResponseDto(accessToken, refreshToken, userName, userEmail);
            return ResponseEntity.ok(tokenResponse);

        } catch (Exception e) {
            System.out.println("❌ [AuthController] 인증 실패: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 실패: 아이디 또는 비밀번호를 확인해주세요.");
        }
    }
}

