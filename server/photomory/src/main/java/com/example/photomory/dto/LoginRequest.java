package com.example.photomory.dto;

public class LoginRequest {
    private String useremail;
    private String password;

    LoginRequest(String useremail, String password) {
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
