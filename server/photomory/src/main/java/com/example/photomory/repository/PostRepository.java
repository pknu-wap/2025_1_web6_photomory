package com.example.photomory.repository;

import com.example.photomory.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // 페이징을 지원하는 앨범 ID로 게시물 조회 메서드
    Page<Post> findByAlbum_AlbumId(Integer albumId, Pageable pageable);
    List<Post> findByAlbum_MyAlbum_MyalbumId(Long groupId);
    // 예시: groupId로 캘린더 태그 리스트 조회 (원하는 리턴 타입에 맞춰 수정 필요)

}
