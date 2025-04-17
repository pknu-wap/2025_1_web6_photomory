package com.example.photomory.service;

import com.example.photomory.config.SecurityConfig;
import com.example.photomory.dto.RegisterRequestDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.mapper.UserMapper;
import com.example.photomory.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RegisterService {

    UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    List<String> specialCharacters = Arrays.asList(
            "!", "@", "#", "$", "%", "^", "&", "*", ".", ",", "₩", "~", "?", "/"
    );
    List<String> nums = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    );

    public String register(RegisterRequestDto userRequestDto) {
        int specialCharacterCount = 0;
        int numCount = 0;
        for (String specialCharacter : specialCharacters) { // 특수문자 포함여부(2자이상) + 중복 체크하기
                if (userRequestDto.getUser_password().contains(specialCharacter)) {
                specialCharacterCount++;
            }
        }
        for (String num : nums) { // 숫자포함 여부(4자이상)
            if (userRequestDto.getUser_password().contains(num)) {
                numCount++;
            }
        }
        if ( // 최종 조건들 만족 여부
                numCount >= 4 &&
                specialCharacterCount >= 2 &&
                userRequestDto.getUser_password().length() >= 12 &&
                userRequestDto.getUser_email().contains("@")
        ) {
            // 1.엔티티 형식 변환
            UserMapper userMapper = new UserMapper();
            SecurityConfig securityConfig = new SecurityConfig();
            UserEntity userEntity = userMapper.userMapper(userRequestDto);
            // 2.비번인코딩

            // 3.리포지토리 저장
            //4. 회원가입 여부 반환
            return "회원가입 완료";

        } else {
            return "회원가입 실패(양식에 맞지 않음)";
        }
    }
}
