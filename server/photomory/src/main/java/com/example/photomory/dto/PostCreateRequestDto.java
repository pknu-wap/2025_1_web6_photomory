package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class PostCreateRequestDto {

    private String postTitle;
    private String postContent;
    private String postDescription;
    private String postImageUrl;
    private String location; // location 필드 추가

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postTime;

    // 기본 생성자
    public PostCreateRequestDto() {
    }

    // 모든 필드를 포함하는 생성자 (postDescription, location 포함)
    public PostCreateRequestDto(String postTitle, String postContent, String postDescription, String postImageUrl, String location, LocalDateTime postTime) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postDescription = postDescription;
        this.postImageUrl = postImageUrl;
        this.location = location; // location 필드 초기화
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

    public String getLocation() { // location 필드의 Getter 추가
        return location;
    }

    public void setLocation(String location) { // location 필드의 Setter 추가
        this.location = location;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }
}