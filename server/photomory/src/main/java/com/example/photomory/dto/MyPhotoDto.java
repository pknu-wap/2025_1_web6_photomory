package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime myphotoMakingtime;
}
