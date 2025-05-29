package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime; // LocalDateTime 추가 임포트

public class PostCreateRequestDto {

    private String postTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate postTime;

    // --- 새로 추가된 필드 ---
    private String photoName; // 사진 파일 이름
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // ISO 8601 형식
    private LocalDateTime photoMakingTime; // 사진 촬영 시간
    // -------------------------

    // 기본 생성자
    public PostCreateRequestDto() {
    }

    // 모든 필드를 포함하는 생성자 (선택 사항, 필요에 따라 추가)
    public PostCreateRequestDto(String postTitle, LocalDate postTime, String photoName, LocalDateTime photoMakingTime) {
        this.postTitle = postTitle;
        this.postTime = postTime;
        this.photoName = photoName;
        this.photoMakingTime = photoMakingTime;
    }

    // 기존 필드에 대한 Getter 및 Setter
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

    // --- 새로 추가된 필드에 대한 Getter 및 Setter ---
    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public LocalDateTime getPhotoMakingTime() {
        return photoMakingTime;
    }

    public void setPhotoMakingTime(LocalDateTime photoMakingTime) {
        this.photoMakingTime = photoMakingTime;
    }
    // ------------------------------------------------
}