package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String userName;
    private String userPassword;
    private String userEmail;
    private String userJob;
    private String userField;
    private String userEquipment;
    private String userIntroduction;
    private Integer userFriends;
    private String userPhotourl;

    public Long getUserIdAsLong() {
        return userId != null ? userId.longValue() : null;
    }
}
