package com.example.photomory.service;

import com.example.photomory.dto.RegisterRequestDto;
import com.example.photomory.mapper.UserMapper;
import com.example.photomory.repository.AuthUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.photomory.entity.UserEntity;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterService {

    private final AuthUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterService(AuthUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final List<String> specialCharacters = Arrays.asList(
            "!", "@", "#", "$", "%", "^", "&", "*", ".", ",", "₩", "~", "?", "/"
    );
    private final List<String> nums = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    );

    public String register(RegisterRequestDto userRequestDto) {
        // 이메일 중복 체크
        Optional<UserEntity> existingUser = userRepository.findByUserEmail(userRequestDto.getUser_email());
        if (existingUser.isPresent()) {
            return "회원가입 실패(이미 존재하는 이메일입니다.)";
        }

        int specialCharacterCount = 0;
        int numCount = 0;
        for (String specialCharacter : specialCharacters) {
            if (userRequestDto.getUser_password().contains(specialCharacter)) {
                specialCharacterCount++;
            }
        }
        for (String num : nums) {
            if (userRequestDto.getUser_password().contains(num)) {
                numCount++;
            }
        }

        if (
                numCount >= 4 &&
                        specialCharacterCount >= 2 &&
                        userRequestDto.getUser_password().length() >= 12 &&
                        userRequestDto.getUser_email().contains("@")
        ) {
            // 1. 엔티티 형식 변환
            UserMapper userMapper = new UserMapper();
            UserEntity userEntity = userMapper.userMapper(userRequestDto);

            // 2. 비밀번호 인코딩
            String encodedPassword = passwordEncoder.encode(userEntity.getUserPassword());
            userEntity.setUserPassword(encodedPassword);

            // 3. 리포지토리 저장
            try {
                userRepository.save(userEntity);
                // 4. 회원가입 여부 반환
                return "회원가입 완료";
            } catch (Exception e) {
                // 데이터베이스 저장 실패 시 예외 처리
                return "회원가입 실패(데이터베이스 오류 발생)";
            }

        } else {
            return "회원가입 실패(양식에 맞지 않음)";
        }
    }
}