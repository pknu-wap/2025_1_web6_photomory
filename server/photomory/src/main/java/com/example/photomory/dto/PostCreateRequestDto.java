package com.example.photomory.dto;

import java.time.LocalDateTime;

public class PostCreateRequestDto {

    private String postTitle;
    private String postContent;
    private String postDescription;   // 새로 추가
    private String postImageUrl;
    private LocalDateTime postTime;

    // 기본 생성자
    public PostCreateRequestDto() {
    }

    // 생성자 (postDescription 포함)
    public PostCreateRequestDto(String postTitle, String postContent, String postDescription, String postImageUrl, LocalDateTime postTime) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postDescription = postDescription;
        this.postImageUrl = postImageUrl;
        this.postTime = postTime;
    }

    // Getter & Setter
    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }
}
