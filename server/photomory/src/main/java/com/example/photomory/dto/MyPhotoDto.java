package com.example.photomory.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MyPhotoDto {
    private Long myphotoId;
    private String myphotoUrl;
    private String myphotoName;
    private String mycomment;
    private LocalDateTime myphotoMakingtime;
}
