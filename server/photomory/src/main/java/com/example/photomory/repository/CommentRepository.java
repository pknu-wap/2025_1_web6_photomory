package com.example.photomory.repository;

import com.example.photomory.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // OurPost에 달린 댓글 조회
    List<Comment> findByOurPost_PostId(Integer postId);

    // EveryPost에 달린 댓글 조회
    List<Comment> findByEveryPost_PostId(Integer postId);

    // OurAlbum + OurPost 조합으로 댓글 조회
    List<Comment> findByOurAlbum_AlbumIdAndOurPost_PostId(Integer albumId, Integer postId);

    // EveryAlbum + EveryPost 조합으로 댓글 조회
    List<Comment> findByEveryAlbum_AlbumIdAndEveryPost_PostId(Integer albumId, Integer postId);

    // OurPost 여러 개에 달린 댓글 한꺼번에 조회
    List<Comment> findByOurPost_PostIdIn(Collection<Long> ourPostIds);

    // 삭제 메서드 - ourPost 기준
    @Transactional
    void deleteByOurPost_PostId(Integer postId);

    // 삭제 메서드 - everyPost 기준
    @Transactional
    void deleteByEveryPost_PostId(Integer postId);
}

