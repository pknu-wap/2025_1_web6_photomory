package com.example.photomory.dto;

import com.example.photomory.entity.Photo; // Photo 엔티티 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Getter, Setter, toString 등을 자동 생성
@NoArgsConstructor // 인자 없는 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
@Builder // 빌더 패턴 사용 가능
public class PhotoResponseDto {
    private Long photoId;
    private String photoUrl;
    private String photoName;
    private String photoMakingTime; // 날짜를 문자열로

    // Photo 엔티티를 받아서 PhotoResponseDto로 변환하는 정적 팩토리 메서드
    public static PhotoResponseDto fromEntity(Photo photo) {
        return PhotoResponseDto.builder()
                .photoId(photo.getPhotoId() != null ? photo.getPhotoId().longValue() : null)
                .photoUrl(photo.getPhotoUrl())
                .photoName(photo.getPhotoName())
                // LocalDateTime을 String으로 변환
                .photoMakingTime(photo.getPhotoMakingTime() != null ? photo.getPhotoMakingTime().toString() : null)
                .build();
    }
}