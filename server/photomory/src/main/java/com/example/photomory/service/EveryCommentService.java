package com.example.photomory.service;

import com.example.photomory.domain.NotificationType;
import com.example.photomory.dto.EveryCommentRequestDto;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EveryCommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public void addComment(EveryCommentRequestDto dto, String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저 못 찾음"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글 못 찾음"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .commentText(dto.getCommentsText())
                .commentTime(LocalDateTime.now())
                .build();
        commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        // 알림 전송
        UserEntity postWriter = post.getUser(); // 게시글 작성자
        if (!user.getUserId().equals(postWriter.getUserId())) { // 자기 댓글은 알림 안 보냄
            // 게시글 제목 (사진의 title)
            String postTitle = post.getPhotos().stream()
                    .map(photo -> photo.getTitle())
                    .findAny()
                    .orElse("제목 없음");

            // 알림 메시지 생성
            String message = user.getUserName() + "님이 " + postTitle + " 게시글에 댓글을 남겼습니다.";

            // 알림 전송
            notificationService.sendNotification(
                    postWriter.getUserId(),      // 수신자: 게시글 작성자
                    user.getUserId(),            // 발신자: 댓글 작성자
                    message,                     // 알림 메시지
                    NotificationType.COMMENT,    // 알림 타입
                    post.getPostId().longValue() // 게시글 ID를 requestId로 사용
            );
        }

    }

}
