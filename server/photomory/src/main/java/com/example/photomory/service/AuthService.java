package com.example.photomory.service;

import com.example.photomory.dto.LoginRequestDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String Auth(LoginRequestDto loginRequestDto) {
        Optional<UserEntity> email = userRepository.findByEmail(loginRequestDto.getUseremail());
        if (email.isEmpty()) { // Optional.empty()인 경우
            return "존재하지 않는 이메일입니다.";

        } else if (email.isEmpty()) { //DB에 비밀번호가 있지만 비밀번호가 틀렸을 때
        }
        return "로그인 실패";
    }

}
