package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostZoomDetailResponseDto {

    private Integer postId;
    private String postText;
    private String postDescription;
    private String photoUrl;
    // private Integer likesCount; // 좋아요 수 필드 제거
    private List<CommentResponseDto> comments;

    public static PostZoomDetailResponseDto from(Post post, List<Comment> comments) {
        PostZoomDetailResponseDto dto = new PostZoomDetailResponseDto();
        dto.setPostId(post.getPostId());
        dto.setPostText(post.getPostText());
        dto.setPostDescription(post.getPostDescription());
        dto.setPhotoUrl(post.getPhotoUrl());
        // dto.setLikesCount(post.getLikesCount()); // 좋아요 수 설정 라인 제거
        dto.setComments(comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList()));
        return dto;
    }
}