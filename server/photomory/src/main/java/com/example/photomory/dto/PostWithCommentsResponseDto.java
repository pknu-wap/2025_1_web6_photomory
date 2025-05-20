package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostWithCommentsResponseDto {
    private Integer postId;
    private String postText;  // 필드명 실제 Post 엔티티와 맞춤
    private List<CommentResponseDto> comments;

    public static PostWithCommentsResponseDto fromEntity(Post post, List<Comment> comments) {
        PostWithCommentsResponseDto dto = new PostWithCommentsResponseDto();
        dto.postId = post.getPostId();
        dto.postText = post.getPostText();  // 실제 필드명 반영
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

    public String getPostText() {
        return postText;
    }
    public void setPostText(String postText) {
        this.postText = postText;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }
    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }
}
