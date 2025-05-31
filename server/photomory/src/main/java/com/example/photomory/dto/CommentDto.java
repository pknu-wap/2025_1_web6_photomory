package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private Integer postId; //
    private Long userId;
    private String userName;
    private String commentsText;
    // private String createdDate;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId() != null ? comment.getCommentId().longValue() : null)
                .postId(comment.getEveryPost() != null ? comment.getEveryPost().getPostId() : null)
                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                .userName(comment.getUser() != null ? comment.getUser().getUserName() : "알 수 없음")
                .commentsText(comment.getCommentText())
                .build();
    }
}