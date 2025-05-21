package com.example.photomory.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USERS")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

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

    public UserEntity() {}

    public UserEntity(
            String userEmail,
            String userName,
            String userPassword,
            String userPhotourl,
            String userEquipment,
            String userIntroduction,
            String userJob,
            String userField
    ) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhotourl = userPhotourl;
        this.userEquipment = userEquipment;
        this.userIntroduction = userIntroduction;
        this.userJob = userJob;
        this.userField = userField;
    }

    // ✅ Getter
    public int getUserId() { return this.userId; }

    public String getUserName() { return this.userName; }

    public String getUserEmail() { return this.userEmail; }

    public String getUserPassword() { return this.userPassword; }

    public String getUserPhotourl() { return this.userPhotourl; }

    public String getUserEquipment() { return this.userEquipment; }

    public String getUserIntroduction() { return this.userIntroduction; }

    public String getUserJob() { return this.userJob; }

    public String getUserField() { return this.userField; }

    // ✅ Setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhotourl(String userPhotourl) {
        this.userPhotourl = userPhotourl;
    }

    public void setUserEquipment(String userEquipment) {
        this.userEquipment = userEquipment;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public void setUserField(String userField) {
        this.userField = userField;
    }

    // ✅ UserDetails interface 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userJob));
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
