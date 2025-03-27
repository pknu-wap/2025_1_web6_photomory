package com.example.photomory.dto;

public class UserResponseDto {
    private int user_id;
    private String user_name;
    private String user_email;
    private String user_password;
    private String job;
    private String field;
    private String equipment;
    private String introduction;

    public UserResponseDto(
            int user_id,
            String user_name,
            String user_email,
            String user_password,
            String user_job,
            String field,
            String equipment,
            String introduction
    ) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.job = user_job;
        this.field = field;
        this.equipment = equipment;
        this.introduction = introduction;
    }

}
