package com.example.photomory.dto;

import com.example.photomory.entity.Comment; // Comment 엔티티 경로 확인
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EveryCommentResponseDto {
    private Long commentId;
    private String commentsText;
    private LocalDateTime commentMakingTime; // 엔티티에 필드가 있다고 가정
    private Long userId;
    private String userNickname; // UserEntity에 nickname 필드가 있다고 가정

    public static EveryCommentResponseDto fromEntity(Comment comment) {
        return EveryCommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .commentsText(comment.getCommentsText())
                .commentMakingTime(comment.getCommentMakingTime())
                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                .userNickname(comment.getUser() != null ? comment.getUser().getNickname() : null)
                .build();
    }
}