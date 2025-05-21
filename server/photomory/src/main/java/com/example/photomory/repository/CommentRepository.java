package com.example.photomory.repository;

import com.example.photomory.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시물(postId)에 달린 댓글 조회
    List<Comment> findByPost_PostId(Long postId);

    // 특정 앨범(albumId) 내 특정 게시물(postId)의 댓글 조회
    List<Comment> findByAlbum_AlbumIdAndPost_PostId(Long albumId, Long postId);

    List<Comment> findByPost_PostIdIn(List<Long> postIds);

}
