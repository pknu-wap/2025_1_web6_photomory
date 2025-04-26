package com.example.photomory.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MyPhotoDto {
    private Long photoId;
    private String photoUrl;
    private String photoName;
    private String comment;
    private LocalDateTime photoMakingTime;
}
