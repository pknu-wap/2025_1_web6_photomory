package com.example.photomory.controller;

import com.example.photomory.dto.LoginRequestDto;
import com.example.photomory.dto.TokenResponseDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = "https://photomory.o-r.kr",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)

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

            // ✅ 안전하게 CustomUserDetails로 변환
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserEntity user = userDetails.getUser();

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
