package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {
    private String user_name;
    private String user_introduction;
    private String user_job;
    private String user_equipment;
    private String user_field;
    private String user_photourl;
}
