package com.example.photomory.repository;

import com.example.photomory.entity.Tag;
import com.example.photomory.entity.Post; // Post 임포트 (findByPost 메서드를 위해 필요)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByTagName(String tagName);

    List<Tag> findByPosts(Post post); // posts 컬렉션 필드에 맞춤
}
