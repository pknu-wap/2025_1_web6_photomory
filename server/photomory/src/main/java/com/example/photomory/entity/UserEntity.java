package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.Objects; // Objects 클래스 임포트

@Entity
@Setter
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_photourl")
    private String userPhotourl;

    @Column(name = "user_equipment", nullable = false)
    private String userEquipment;

    @Column(name = "user_introduction", columnDefinition = "TEXT")
    private String userIntroduction;

    @Column(name = "user_job", nullable = false)
    private String userJob;

    @Column(name = "user_field", nullable = false)
    private String userField;

    @Column(name = "user_area")
    private String userArea;



    public UserEntity() {}

    public UserEntity(
            String userEmail,
            String userName,
            String userPassword,
            String userPhotourl,
            String userEquipment,
            String userIntroduction,
            String userJob,
            String userField,
            String userArea
    ) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhotourl = userPhotourl;
        this.userEquipment = userEquipment;
        this.userIntroduction = userIntroduction;
        this.userJob = userJob;
        this.userField = userField;
        this.userArea = userArea;
    }

    // Setter for userPassword (기존 코드)
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    // Getters (기존 코드)
    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhotourl() {
        return userPhotourl;
    }

    public String getUserEquipment() {
        return userEquipment;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public String getUserJob() {
        return userJob;
    }

    public String getUserField() {
        return userField;
    }

    public String getUserArea() {return userArea;}




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        // userId가 null일 수 있으므로 Objects.equals를 사용합니다.
        // 영속성 컨텍스트에 없는 엔티티는 userId가 null일 수 있기 때문입니다.
        // 하지만 일반적으로 PK는 null이 아니어야 합니다.
        // DB에서 조회된 엔티티는 userId가 항상 존재하므로, userId만 비교하는 것이 가장 견고합니다.
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        // userId가 null일 수 있으므로 Objects.hash를 사용합니다.
        return Objects.hash(userId);
    }

}