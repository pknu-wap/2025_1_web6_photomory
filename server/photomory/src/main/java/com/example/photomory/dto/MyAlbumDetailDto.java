package com.example.photomory.dto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.example.photomory.dto.MyPhotoDto;

@Data
@Builder
public class MyAlbumDetailDto {
    private Long albumId;
    private Long userId;
    private String albumName;
    private String albumDescription;
    private LocalDateTime albumMakingTime;
    private List<MyPhotoDto> photos;
    private List<String> tags;
}
