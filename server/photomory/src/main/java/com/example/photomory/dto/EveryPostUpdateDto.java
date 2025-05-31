package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EveryPostUpdateDto {
    private String postText;
    private String postDescription;
    private String location;
    private MultipartFile photo;
    private String photoName;
    private String photoMakingTime;
    private String tagsJson; // JSON string like ["일상", "여행"]
}
