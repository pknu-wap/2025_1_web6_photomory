package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Tag; // Tag 엔티티 임포트 추가
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Lombok AllArgsConstructor 임포트 추가

import java.time.LocalDateTime;
import java.util.List; // List 임포트
import java.util.stream.Collectors; // Collectors 임포트
import java.util.Arrays;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Lombok AllArgsConstructor (필요하다면)
public class AlbumResponseDto {

    private Integer albumId;
    private String albumName;
    // private String albumTag; // 기존 단일 태그 필드 제거
    private List<String> albumTags; // 여러 개의 태그를 담을 리스트 필드 추가
    private LocalDateTime albumMakingTime;
    private String albumDescription;
    private Integer groupId; // 앨범이 속한 그룹의 ID 추가

    // 엔티티 → DTO 변환 메서드

    public static AlbumResponseDto fromEntity(Album album) {
        AlbumResponseDto dto = new AlbumResponseDto();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumName(album.getAlbumName());

        if (album.getAlbumTag() != null && !album.getAlbumTag().isEmpty()) {
            List<String> tags = Arrays.stream(album.getAlbumTag().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            dto.setAlbumTags(tags);
        } else {
            dto.setAlbumTags(List.of());
        }

        dto.setAlbumMakingTime(album.getAlbumMakingTime());
        dto.setAlbumDescription(album.getAlbumDescription());

        if (album.getMyAlbum() != null) {
            dto.setGroupId(album.getMyAlbum().getMyalbumId());
        }

        return dto;
    }
}