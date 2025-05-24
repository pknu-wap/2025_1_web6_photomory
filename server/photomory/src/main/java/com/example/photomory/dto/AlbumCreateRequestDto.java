package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AlbumCreateRequestDto {
    private String albumName;
    private String albumTag;
    private LocalDateTime albumMakingTime;
    private String albumDescription;
}