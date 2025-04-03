package com.example.photomory.service;

import com.example.photomory.dto.UserRequestDto;
import com.example.photomory.dto.UserResponseDto;
import com.example.photomory.repository.UserRepository;

public class UserService {

    public UserResponseDto register(UserRequestDto userRequestDto) {
        if (userRequestDto.getUser_password().length() > 10) {
            return new UserResponseDto(
                    userRequestDto.getUser_id(),
                    userRequestDto.getUser_email(),
                    userRequestDto.getUser_password(),
                    userRequestDto.getEquipment(),
                    userRequestDto.getJob(),
                    userRequestDto.getField(),
                    userRequestDto.getEquipment(),
                    userRequestDto.getIntroduction()
            );
        } else {
            return null;
        }
    }
}
