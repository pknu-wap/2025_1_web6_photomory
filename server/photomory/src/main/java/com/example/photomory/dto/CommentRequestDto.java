package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Integer albumId;
    private Integer postId;
    private Long userId;
    private String commentsText;
    private Integer commentCount;  // 추가된 필드
}
