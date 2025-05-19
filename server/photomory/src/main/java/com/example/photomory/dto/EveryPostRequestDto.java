package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EveryPostRequestDto {
    private Long userId;
    private String postText;
    private String postDescription;
    private String location;

    private String photoUrl;
    private String photoName;
    private String photoComment;
    private LocalDateTime photoMakingTime;

    private List<String> tags;
}
