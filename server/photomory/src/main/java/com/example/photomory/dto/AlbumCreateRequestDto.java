package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AlbumCreateRequestDto {
    private String albumName;
    private List<String> albumTags;  // 여기서 배열(리스트)로 받음
    private LocalDateTime albumMakingTime;
    private String albumDescription;
}
