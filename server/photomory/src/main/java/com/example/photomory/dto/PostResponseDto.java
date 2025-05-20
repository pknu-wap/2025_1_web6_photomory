package com.example.photomory.dto;

import com.example.photomory.entity.Post;

public class PostResponseDto {
    private Integer postId;
    private String postText;  // 필드명 실제 Post 엔티티와 맞춤

    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.postId = post.getPostId();
        dto.postText = post.getPostText();  // 실제 필드명 반영
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
}
