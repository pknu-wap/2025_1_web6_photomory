package com.example.photomory.entity;

import com.example.photomory.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name", nullable = false)
    private String user_name;

    @Column(name = "user_email", nullable = false, unique = true)
    private String user_email;

    @Column(name = "user_password", nullable = false)
    private String user_password;

    @Column(name = "user_photourl")
    private String user_photourl;

    @Column(name = "user_equipment", nullable = false)
    private String user_equipment;

    @Column(name = "user_introduction", columnDefinition = "TEXT")
    private String user_introduction;

    @Column(name = "user_job", nullable = false)
    private String user_job;

    @Column(name = "user_field", nullable = false)
    private String user_field;

    public void register(UserRequestDto userRequestDto) { //기본키로 지정해서 자동으로 MySQL에서 1로 시작하게 함.
        this.user_name = userRequestDto.getUser_name();
        this.user_email = userRequestDto.getUser_email();
        this.user_password = userRequestDto.getUser_password();
        this.user_photourl = userRequestDto.getUser_photourl();
        this.user_job = userRequestDto.getUser_job();
        this.user_equipment = userRequestDto.getUser_equipment();
        this.user_introduction = userRequestDto.getUser_introduction();
        this.user_field = userRequestDto.getUser_field();

    }
}
