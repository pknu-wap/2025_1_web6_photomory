package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class PostCreateRequestDto {

    private String postTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate postTime;

    private String photoName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate photoMakingTime;

    public PostCreateRequestDto() {
    }

    public PostCreateRequestDto(String postTitle, LocalDate postTime, String photoName, LocalDate photoMakingTime) {
        this.postTitle = postTitle;
        this.postTime = postTime;
        this.photoName = photoName;
        this.photoMakingTime = photoMakingTime;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public LocalDate getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDate postTime) {
        this.postTime = postTime;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public LocalDate getPhotoMakingTime() {
        return photoMakingTime;
    }

    public void setPhotoMakingTime(LocalDate photoMakingTime) {
        this.photoMakingTime = photoMakingTime;
    }
}
