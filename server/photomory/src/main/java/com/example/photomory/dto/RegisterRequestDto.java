package com.example.photomory.dto;

public class RegisterRequestDto {

    private String user_name;
    private String user_email;
    private String user_password;
    private String user_photourl;
    private String user_job;
    private String user_equipment;
    private String user_introduction;
    private String user_field;

    RegisterRequestDto(
            String user_name,
            String user_email,
            String user_password,
            String user_photourl,
            String user_equipment,
            String user_introduction,
            String user_job,
            String user_field
    ) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_photourl = user_photourl;
        this.user_equipment = user_equipment;
        this.user_introduction = user_introduction;
        this.user_job = user_job;
        this.user_field = user_field;
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

    public String getUser_photourl() {
        return user_photourl;
    }

    public String getUser_equipment() {
        return user_equipment;
    }

    public String getUser_introduction() {
        return user_introduction;
    }

    public String getUser_job() {
        return user_job;
    }

    public String getUser_field() {
        return user_field;
    }
}