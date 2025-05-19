package com.example.photomory.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MyAlbumDetailDto {
    private Long myalbumId;
    private Long userId;
    private String myalbumName;
    private String myalbumDescription;
    private LocalDateTime myalbumMakingtime;
    private List<MyPhotoDto> myphotos;
    private List<String> mytags;
}
