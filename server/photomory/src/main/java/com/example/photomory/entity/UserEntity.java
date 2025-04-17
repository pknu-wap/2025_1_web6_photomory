package com.example.photomory.entity;

import com.example.photomory.dto.RegisterRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public UserEntity(
            String user_email,
            String user_name,
            String user_password,
            String user_photourl,
            String user_equipment,
            String user_introduction,
            String user_job,
            String user_field
    ) {
    }
}
