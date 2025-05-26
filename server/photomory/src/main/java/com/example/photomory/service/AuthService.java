package com.example.photomory.service;

import com.example.photomory.dto.LoginRequestDto;
import com.example.photomory.repository.AuthUserRepository;
import com.example.photomory.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.photomory.entity.UserEntity;


import java.util.Optional;

@Service
public class AuthService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //hw수정
    private final JwtTokenProvider jwtTokenProvider;


    // 생성자 주입 방식으로 passwordEncoder 주입
    //hw PasswordEncoder만 수정 총 2개
    public AuthService(AuthUserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String Auth(LoginRequestDto loginRequestDto) {
        // 이메일로 사용자 찾기
        Optional<UserEntity> userEntityOpt = userRepository.findByUserEmail(loginRequestDto.getUseremail());

        if (userEntityOpt.isEmpty()) {
            return "존재하지 않는 이메일입니다.";
        }

        UserEntity userEntity = userEntityOpt.get();

        // 비밀번호 비교
        if (passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getUserPassword())) {
            return "로그인 성공";
        } else {
            return "로그인 실패(비밀번호가 틀렸습니다.)";
        }
    }

    //hw
    public Long extractUserId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtTokenProvider.extractUsername(token);
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }
}
