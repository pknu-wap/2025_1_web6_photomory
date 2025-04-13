package com.example.photomory.service;

import com.example.photomory.dto.UserRequestDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    List<String> specialCharacters = Arrays.asList(
            "!", "@", "#", "$", "%", "^", "&", "*", ".", ",", "₩", "~", "?", "/"
    );
    List<String> nums = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    );

    public String register(UserRequestDto userRequestDto) {
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
            UserEntity registerEntity = new UserEntity();
            registerEntity.register(userRequestDto);
            userRepository.save(registerEntity); //15번 줄에서 선언
            return "회원가입 완료";

        } else {
            return "회원가입 실패(양식에 맞지 않음)";
        }
    }
}
