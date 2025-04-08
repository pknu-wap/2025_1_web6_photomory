package com.example.photomory.service;

import com.example.photomory.dto.UserRequestDto;

public class UserService {

    public String register(UserRequestDto userRequestDto) {
        if (userRequestDto.getUser_password().length() > 10) {

            return "회원가입 완료!";
        }
        return "조건이 맞지 않음";
    }
}
