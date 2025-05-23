package com.example.photomory.dto;

import lombok.Getter; // Lombok Getter 임포트
import lombok.Setter; // Lombok Setter 임포트
import lombok.NoArgsConstructor; // Lombok NoArgsConstructor 임포트
import lombok.AllArgsConstructor; // Lombok AllArgsConstructor 임포트 (필요하다면)

import java.time.LocalDateTime;
import java.util.List; // List 임포트

@Getter
@Setter
@NoArgsConstructor // Lombok NoArgsConstructor
@AllArgsConstructor // Lombok AllArgsConstructor (필요하다면)
public class AlbumCreateRequestDto {

    private String albumName;
    // private String albumTag; // 단일 태그 필드 제거
    private List<String> albumTags; // 여러 개의 태그를 담을 리스트 필드 추가
    private LocalDateTime albumMakingTime;
    private String albumDescription;

}