package com.example.photomory.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EveryCommentDto {
    private Long userId;
    private String userName;
    private String userPhotourl;
    private String commentText;
}