package com.example.photomory.entity;

import com.example.photomory.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {
    public User() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "field", nullable = false)
    private String field;

    @Column(name = "equipment", nullable = false)
    private String equipment;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "url")
    private String url;

    public User(UserRequestDto userRequestDto) { //기본키로 지정해서 자동으로 MySQL에서 1로 시작하게 함.
        this.userName = userRequestDto.getUser_name();
        this.userEmail = userRequestDto.getUser_email();
        this.userPassword = userRequestDto.getUser_password();
        this.url = userRequestDto.getUrl();
        this.job = userRequestDto.getJob();
        this.field = userRequestDto.getField();
        this.equipment = userRequestDto.getEquipment();
        this.introduction = userRequestDto.getIntroduction();
    }
}
