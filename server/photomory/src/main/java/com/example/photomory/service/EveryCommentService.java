package com.example.photomory.service;

import com.example.photomory.domain.NotificationType;
import com.example.photomory.dto.EveryCommentRequestDto;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.EveryPost;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.EveryPostRepository;
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
    private final EveryPostRepository everyPostRepository;  // 변경
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public void addComment(EveryCommentRequestDto dto, String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저 못 찾음"));

        EveryPost post = everyPostRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글 못 찾음"));

        Comment comment = Comment.builder()
                .user(user)
                .everyPost(post)  // 여기 post는 EveryPost 타입입니다.
                .commentText(dto.getCommentsText())
                .commentTime(LocalDateTime.now())
                .build();
        commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);
        everyPostRepository.save(post);

        //알림 전송 (postText를 그대로 제목으로 사용!)
        UserEntity postWriter = post.getUser();
        if (!user.getUserId().equals(postWriter.getUserId())) {
            String postText = post.getPostText() != null && !post.getPostText().isEmpty()
                    ? post.getPostText()
                    : "제목 없음";

            String message = user.getUserName() + "님이 '" + postText + "' 게시글에 댓글을 남겼습니다.";

            notificationService.sendNotification(
                    postWriter.getUserId(),
                    user.getUserId(),
                    message,
                    NotificationType.COMMENT,
                    post.getPostId().longValue()
            );
        }
    }
}
