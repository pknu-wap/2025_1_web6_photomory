package com.example.photomory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // @Query new com.example.photomory.dto.CalendarTagResponseDto(...) 생성자 사용을 위해 필요
public class CalendarTagResponseDto {
    private LocalDateTime postMakingTime;
    private String photoUrl;
    private String postDescription;


}