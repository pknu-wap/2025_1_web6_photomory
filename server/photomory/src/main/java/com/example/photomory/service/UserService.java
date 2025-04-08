package com.example.photomory.service;

import com.example.photomory.dto.UserRequestDto;
import com.example.photomory.entity.RegisterEntity;

import java.util.Arrays;
import java.util.List;

public class UserService {
    List<String> specialCharacters = Arrays.asList(
            "!", "@", "#", "$", "%", "^", "&", "*", ".", ",", "₩", "~", "?", "/"
    );
    List<String> nums = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    );
    public String register(UserRequestDto userRequestDto) {
        int specialCharacterCount = 0;
        int numCount = 0;
        for (String specialCharacter : specialCharacters) { // 특수문자 포함여부(2자이상)
            if (userRequestDto.getUser_password().contains(specialCharacter)) {
                specialCharacterCount++;
            }
        }
        for (String num : nums) { // 숫자포함 여부(4자이상)
            if (userRequestDto.getUser_password().contains(num)) {
                numCount++;
            }
        }
        if (
                numCount >= 4 &&
                specialCharacterCount >= 2 &&
                userRequestDto.getUser_password().length() >= 12
        ) {
            RegisterEntity registerEntity = new RegisterEntity();
            registerEntity.register(userRequestDto);
            return "회원가입 완료";
        } else {
            return "양식에 맞지 않음";
        }
    }
}
