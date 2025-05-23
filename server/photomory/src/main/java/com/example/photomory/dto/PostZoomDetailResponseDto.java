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
    private List<CommentResponseDto> comments;

    public static PostZoomDetailResponseDto from(Post post, List<Comment> comments) {
        PostZoomDetailResponseDto dto = new PostZoomDetailResponseDto();
        dto.setPostId(post.getPostId());
        dto.setPostText(post.getPostText());
        dto.setPostDescription(post.getPostDescription());
        dto.setPhotoUrl(post.getPhotoUrl());
        dto.setComments(comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList()));
        return dto;
    }
}
