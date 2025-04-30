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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // 클래스 레벨 매핑: /api/auth/login
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
            // 1. 이메일과 비밀번호로 인증 객체 생성
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUseremail(),
                            loginRequestDto.getPassword()
                    )
            );

            // 2. 인증된 사용자 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. 토큰 생성
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

            // 4. UserDetails에서 UserEntity로 캐스팅하여 이름과 이메일 추출
            UserEntity user = (UserEntity) userDetails;
            String userName = user.getUserName();
            String userEmail = user.getUserEmail();

            // 5. 응답 DTO 생성 및 반환
            TokenResponseDto tokenResponse = new TokenResponseDto(accessToken, refreshToken, userName, userEmail);
            return ResponseEntity.ok(tokenResponse);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 실패: 아이디 또는 비밀번호를 확인해주세요.");
        }
    }
}