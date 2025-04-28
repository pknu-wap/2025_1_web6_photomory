package com.example.photomory.controller;

import com.example.photomory.dto.LoginRequestDto;
import com.example.photomory.dto.TokenResponseDto;
import com.example.photomory.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; // 추가

@RestController
@RequestMapping("/api/auth") // 추가: 클래스 레벨 매핑
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login") // 유지
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUseremail(), loginRequestDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

            TokenResponseDto tokenResponse = new TokenResponseDto(accessToken, refreshToken);
            return ResponseEntity.ok(tokenResponse);

        } catch (Exception e) {
            return new ResponseEntity<>("로그인 실패: 아이디 또는 비밀번호를 확인해주세요.", HttpStatus.UNAUTHORIZED);
        }
    }
}