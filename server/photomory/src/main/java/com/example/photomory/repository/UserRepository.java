package com.example.photomory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.photomory.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(Long userId);
    List<UserEntity> findByUserNameContaining(String keyword);
    Optional<UserEntity> findByUserEmail(String userEmail);
    
}
