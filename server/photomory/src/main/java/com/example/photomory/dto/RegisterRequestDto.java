package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestDto {

    private String user_name;
    private String user_email;
    private String user_password;
    private String user_photourl;
    private String user_job;
    private String user_equipment;
    private String user_introduction;
    private String user_field;

    // 기본 생성자 (public)
    public RegisterRequestDto() {}

    // 생성자에 @JsonCreator와 @JsonProperty 지정
    @JsonCreator
    public RegisterRequestDto(
            @JsonProperty("user_name") String user_name,
            @JsonProperty("user_email") String user_email,
            @JsonProperty("user_password") String user_password,
            @JsonProperty("user_photourl") String user_photourl,
            @JsonProperty("user_job") String user_job,
            @JsonProperty("user_equipment") String user_equipment,
            @JsonProperty("user_introduction") String user_introduction,
            @JsonProperty("user_field") String user_field) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_photourl = user_photourl;
        this.user_job = user_job;
        this.user_equipment = user_equipment;
        this.user_introduction = user_introduction;
        this.user_field = user_field;
    }

    // 게터들
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

    public String getUser_job() {
        return user_job;
    }

    public String getUser_equipment() {
        return user_equipment;
    }

    public String getUser_introduction() {
        return user_introduction;
    }

    public String getUser_field() {
        return user_field;
    }
}
