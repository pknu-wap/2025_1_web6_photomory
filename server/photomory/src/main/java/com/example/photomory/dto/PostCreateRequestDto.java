package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate; // 이 임포트는 더 이상 postTime에서 사용하지 않으므로 필요없을 수 있습니다.
import java.time.LocalDateTime;

public class PostCreateRequestDto {

    private String postTitle;

    // postTime 필드를 LocalDate에서 LocalDateTime으로 변경하고 패턴도 조정
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // <-- 변경: 날짜와 시간 포함
    private LocalDateTime postTime; // <-- 변경: LocalDate -> LocalDateTime

    private String photoName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime photoMakingTime;

    public PostCreateRequestDto() {
    }

    public PostCreateRequestDto(String postTitle, LocalDateTime postTime, String photoName, LocalDateTime photoMakingTime) {
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

    // Getter 및 Setter도 변경된 타입에 맞춰 수정
    public LocalDateTime getPostTime() { // <-- 반환 타입 변경
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) { // <-- 파라미터 타입 변경
        this.postTime = postTime;
    }

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
}