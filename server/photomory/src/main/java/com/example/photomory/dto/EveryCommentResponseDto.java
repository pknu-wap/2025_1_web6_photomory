package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EveryCommentResponseDto {
    private Integer commentId;
    private String commentsText;
    private LocalDateTime commentMakingTime;
    private Long userId;
    private String userName;  // 닉네임 대신 userName

    public static EveryCommentResponseDto fromEntity(Comment comment) {
        return EveryCommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .commentsText(comment.getCommentsText())
                .commentMakingTime(comment.getCreatedAt())
                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                .userName(comment.getUser() != null ? comment.getUser().getUserName() : null) // userName으로 수정
                .build();
    }
}
