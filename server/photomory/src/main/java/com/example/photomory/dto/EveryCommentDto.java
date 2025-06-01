package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EveryCommentDto {
    private Integer commentId;
    private Integer postId; // 게시물 ID
    private String comment; // 댓글 내용
    private LocalDateTime createdAt; // 생성 시간
    private Long userId; // 사용자 ID
    private String userName; // 사용자 이름
    private String userPhotourl; // 사용자 사진 URL

    public static EveryCommentDto from(Comment comment, String userName) {
        return EveryCommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getEveryPost().getPostId())  // everyPost로 바꿈
                .comment(comment.getCommentText())
                .createdAt(comment.getCommentTime())
                .userId(comment.getUser().getUserId())
                .userName(userName)
                .userPhotourl(comment.getUser().getUserPhotourl())
                .build();
    }
}
