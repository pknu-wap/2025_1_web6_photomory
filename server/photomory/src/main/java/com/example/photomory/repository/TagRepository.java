package com.example.photomory.repository;

import com.example.photomory.entity.Tag;
import com.example.photomory.entity.Post; // Post 임포트 (findByPost 메서드를 위해 필요)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> { // Tag 엔티티의 PK는 Integer이므로 Long 대신 Integer로 변경

    // 특정 TagName을 가진 Tag 레코드를 찾음 (앨범 태그처럼 단일 태그 조회 시 유용)
    Optional<Tag> findByTagName(String tagName);

    // Tag 엔티티에 'post' 필드(ManyToOne)가 있으므로 이 쿼리 메서드 사용 가능
    List<Tag> findByPost(Post post);

}