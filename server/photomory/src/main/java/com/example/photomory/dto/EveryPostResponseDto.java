package com.example.photomory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EveryPostResponseDto {
    private Integer postId;
    private Long userId;
    private String userName;
    private String userPhotourl;
    private String postText;
    private String postDescription;
    private Integer likesCount;
    private boolean isLiked;
    private String location;
    private String photoUrl;
    private List<String> tags;
    private Integer commentCount;
    private List<EveryCommentDto> comments;
    private LocalDateTime createdAt;
}
