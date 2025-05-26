package com.example.photomory.service;

import com.example.photomory.entity.Like;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.LikeRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void toggleLike(Long postId, String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
        Post post = postRepository.findById(postId.intValue())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        likeRepository.findByUserAndPost(user, post).ifPresentOrElse(
                likeRepository::delete,
                () -> {
                    Like like = Like.builder()
                            .user(user)
                            .post(post)
                            .commentId(0) // comment 좋아요 안쓸거니까 고정
                            .likesCount(1)
                            .build();
                    likeRepository.save(like);
                }
        );
    }

    public Long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId.intValue())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));
        return likeRepository.countByPost(post);
    }
}
