package com.example.photomory.repository;

import com.example.photomory.entity.UserEntity; //엔티티 불러오기
import org.springframework.data.jpa.repository.JpaRepository;
//jpa 클래스 상속(이거 불러오면 save, findById, findAll, delete 등 다 만들어줌, 구현도 필요없음)
//엔티티형식으로 받을 수 있음.
import org.springframework.stereotype.Repository; // Repository 어노테이션
import java.util.Optional;// Optional


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email); // 로그인 전용 이메일 찾는 메서드
}
