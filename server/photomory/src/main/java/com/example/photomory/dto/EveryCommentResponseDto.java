package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // 추가
import lombok.AllArgsConstructor; // 추가

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor // Lombok Builder 사용 시 필요 (기본 생성자)
@AllArgsConstructor // Lombok Builder 사용 시 필요 (모든 필드를 인자로 받는 생성자)
public class EveryCommentResponseDto {
    private Integer commentId;
    private String commentsText;
    private LocalDateTime commentMakingTime; // DTO에서 사용하는 필드명
    private Long userId;
    private String userName;

    public static EveryCommentResponseDto fromEntity(Comment comment) {
        return EveryCommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .commentsText(comment.getCommentText())
                .commentMakingTime(comment.getCommentTime()) // <<< 이 부분을 수정했습니다.
                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                .userName(comment.getUser() != null ? comment.getUser().getUserName() : null)
                .build();
    }
}