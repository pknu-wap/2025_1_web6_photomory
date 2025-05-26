package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EveryCommentRequestDto {
    private Integer postId;
    private String commentsText;
}
