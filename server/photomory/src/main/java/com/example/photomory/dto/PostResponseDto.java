package com.example.photomory.dto;

import com.example.photomory.entity.Post;

public class PostResponseDto {
    private Integer postId;
    private String content; // title 없으면 content만 써도 됨

    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.postId = post.getPostId();
        dto.content = post.getPostText();  // 실제 필드명으로 바꿔야 함
        return dto;
    }

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
}
