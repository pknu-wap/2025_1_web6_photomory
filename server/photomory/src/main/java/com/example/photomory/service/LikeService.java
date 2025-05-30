package com.example.photomory.service;

import com.example.photomory.domain.NotificationType;
import com.example.photomory.entity.Like;
import com.example.photomory.entity.Photo;
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
    private final NotificationService notificationService;

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

        // noti-hw
        UserEntity postWriter = post.getUser();

        // 작성자 본인 글에 누른 경우는 알림 X
        if (!user.getUserId().equals(postWriter.getUserId())) {
            // 게시글 제목 (사진의 title에서 가져옴)
            String postTitle = post.getPhotos().stream()
                    .map(Photo::getTitle)
                    .findAny()
                    .orElse("제목 없음");

            String message = user.getUserName() + "님이 " + postTitle + " 게시글에 좋아요를 눌렀습니다.";
            notificationService.sendNotification(
                    postWriter.getUserId(),  // 수신자: 게시글 작성자
                    user.getUserId(),        // 발신자: 좋아요 누른 사람
                    message,
                    NotificationType.LIKE,   // 알림 타입
                    postId                   // 게시글 ID
            );
        }

    }

    public Long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId.intValue())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));
        return likeRepository.countByPost(post);
    }
}
