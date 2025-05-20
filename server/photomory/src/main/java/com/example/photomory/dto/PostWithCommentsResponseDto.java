package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostWithCommentsResponseDto {
    private Integer postId;
    private String content; // 실제 필드명에 맞게 수정
    private List<CommentResponseDto> comments;

    public static PostWithCommentsResponseDto fromEntity(Post post, List<Comment> comments) {
        PostWithCommentsResponseDto dto = new PostWithCommentsResponseDto();
        dto.postId = post.getPostId();
        dto.content = post.getPostText(); // 실제 필드명으로 수정
        dto.comments = comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
        return dto;
    }

    // Getter & Setter
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }
}
