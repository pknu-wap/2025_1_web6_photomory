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
    private Integer postId;
    private String comment;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
    private String userPhotourl;

    public static EveryCommentDto from(Comment comment, String userName) {
        return EveryCommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())  // Post 객체에서 postId 꺼냄
                .comment(comment.getCommentText())
                .createdAt(comment.getCommentTime())  // 필드 이름에 맞춰 수정
                .userId(comment.getUser().getUserId())
                .userName(userName)
                .userPhotourl(comment.getUser().getUserPhotourl())
                .build();
    }
}
