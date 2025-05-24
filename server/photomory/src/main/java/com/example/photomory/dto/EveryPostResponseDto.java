package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EveryPostResponseDto {
    private Integer postId;
    private Long userId;
    private String userName;
    private String userPhotourl;
    private String postText;
    private String postDescription;
    private Integer likesCount;
    private String location;
    private String photoUrl;
    private List<String> tags;
    private Integer commentCount;
    private List<EveryCommentDto> comments;
}
