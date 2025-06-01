package com.example.photomory.mapper;

import com.example.photomory.dto.RegisterRequestDto;
import com.example.photomory.entity.UserEntity;


public class UserMapper {

    String user_name;
    String user_email;
    String user_password;
    String user_photourl;
    String user_job;
    String user_equipment;
    String user_introduction;
    String user_field;
    String user_area;

    public UserEntity userMapper(RegisterRequestDto userRequestDto) {
        this.user_name = userRequestDto.getUser_name();
        this.user_email = userRequestDto.getUser_email();
        this.user_password = userRequestDto.getUser_password();
        this.user_photourl = userRequestDto.getUser_photourl();
        this.user_job = userRequestDto.getUser_job();
        this.user_equipment = userRequestDto.getUser_equipment();
        this.user_introduction = userRequestDto.getUser_introduction();
        this.user_field = userRequestDto.getUser_field();
        this.user_area = userRequestDto.getUser_area();

        return new UserEntity(
                user_email,
                user_name,
                user_password,
                user_photourl,
                user_equipment,
                user_introduction,
                user_job,
                user_field,
                user_area
                );
    }
}
