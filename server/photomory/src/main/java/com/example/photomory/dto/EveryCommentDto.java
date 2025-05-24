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
    private String userName;
    private Long userId;
    private String userPhotourl;
    private LocalDateTime createdAt;

    public static EveryCommentDto from(Comment comment, String userName) {
        return EveryCommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .albumId(comment.getAlbumId())
                .comment(comment.getCommentsText())
                .createdAt(comment.getCreatedAt())
                .userName(userName)
                .build();
    }
}
