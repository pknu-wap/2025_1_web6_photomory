package com.example.photomory.repository;

import com.example.photomory.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // 특정 앨범에 속한 모든 포스트 조회용 메서드 추가
    List<Post> findByAlbum_AlbumId(Integer albumId);
}
