package com.example.photomory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(Long userId);
    List<UserEntity> findByUserNameContaining(String keyword);

    //hw-알림창jwt연동할때이메일필요해서 추가
    Optional<UserEntity> findByUserEmail(String userEmail);
}



