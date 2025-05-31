package com.example.photomory.repository;

import com.example.photomory.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> { // Comment의 ID가 Long임을 확인

    // 특정 게시물(postId)에 달린 댓글 조회
    List<Comment> findByPost_PostId(Long postId); // Post ID가 Long임을 확인

    // 특정 앨범(albumId) 내 특정 게시물(postId)의 댓글 조회
    List<Comment> findByAlbum_AlbumIdAndPost_PostId(Long albumId, Long postId); // Album ID와 Post ID가 Long임을 확인

    List<Comment> findByPost_PostIdIn(List<Long> postIds); // Post ID가 Long임을 확인

    @Transactional // 이 메소드에서 데이터 삭제가 발생하므로 @Transactional 어노테이션을 붙여야 합니다.
    void deleteByPost_PostId(Long postId); // Post ID가 Long임을 확인

    void deleteByOurPost_PostId(Integer postId);

    List<Comment> findByOurPost_PostIdIn(Collection<Long> ourPostIds);
}

