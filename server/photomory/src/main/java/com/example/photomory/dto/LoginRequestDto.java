package com.example.photomory.dto;

public class LoginRequestDto {
    private String useremail;
    private String password;

    LoginRequestDto(String useremail, String password) {
        this.useremail = useremail;
        this.password = password;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getPassword() {
        return password;
    }
}
