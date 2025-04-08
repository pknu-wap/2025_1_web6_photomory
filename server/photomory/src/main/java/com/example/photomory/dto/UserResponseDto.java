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
    private String url;

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
    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getJob() {
        return job;
    }

    public String getField() {
        return field;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getIntroduction() {
        return introduction;
    }
}
