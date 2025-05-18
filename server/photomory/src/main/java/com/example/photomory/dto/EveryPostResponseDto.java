package com.example.photomory.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EveryPostResponseDto {
    private Long postId;
    private Long userId;
    private String userName;
    private String userPhotourl;
    private String postText;
    private String postDescription;
    private String location;
    private String photoUrl;
    private int likesCount;
    private List<String> tags;
    private int commentCount;
    private List<EveryCommentDto> comments;
}