package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class
PostCreateRequestDto {

    private String postTitle;
    private String postImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postTime;

    // 기본 생성자
    public PostCreateRequestDto() {
    }

    // 모든 필드를 포함하는 생성자
    public PostCreateRequestDto(String postTitle, String postImageUrl, LocalDateTime postTime) {
        this.postTitle = postTitle;
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
