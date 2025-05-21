package com.example.photomory.dto;

import com.example.photomory.entity.Post;
import com.example.photomory.entity.Comment;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponseDto {
    private Long postId;
    private String postText;
    private String postDescription;
    private String photoUrl;
    private List<CommentDto> comments;

    public static PostDetailResponseDto fromEntity(Post post, List<Comment> comments) {
        return PostDetailResponseDto.builder()
                .postId(post.getPostId())
                .postText(post.getPostText())
                .postDescription(post.getPostDescription())
                .photoUrl(post.getPhotoUrl())
                .comments(comments.stream()
                        .map(CommentDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
