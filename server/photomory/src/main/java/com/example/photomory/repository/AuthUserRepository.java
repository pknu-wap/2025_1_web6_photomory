package com.example.photomory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//jpa 클래스 상속(이거 불러오면 save, findById, findAll, delete 등 다 만들어줌, 구현도 필요없음)
//엔티티형식으로 받을 수 있음.
import org.springframework.stereotype.Repository; // Repository 어노테이션
import java.util.Optional;// Optional
import com.example.photomory.entity.UserEntity;



@Repository
public interface AuthUserRepository extends JpaRepository<UserEntity, Long> { // UserEntity의 ID 타입이 Long이라고 가정하고 수정
    Optional<UserEntity> findByUserEmail(String userEmail); // 메서드명 유지
}