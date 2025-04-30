// UserEntity.java
package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    // 🔐 UserDetails 구현 메서드들
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userJob)); // 예: ROLE_PHOTOGRAPHER
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userEmail; // Spring Security에서 로그인 ID로 사용할 값
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
