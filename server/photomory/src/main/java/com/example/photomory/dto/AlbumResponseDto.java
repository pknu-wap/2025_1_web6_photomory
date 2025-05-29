package com.example.photomory.dto;

import com.example.photomory.entity.Album;
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
@AllArgsConstructor
public class AlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private List<String> albumTags;
    private LocalDateTime albumMakingTime;
    private String albumDescription;
    private Integer groupId;

    // 새로 추가: 사진 리스트 필드
    private List<PostResponseDto> photos;  // PostResponseDto는 사진 DTO 클래스

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

        // 추가: photos 리스트 변환 (Album에 getPosts()가 있다고 가정)
        if (album.getPosts() != null && !album.getPosts().isEmpty()) {
            List<PostResponseDto> photoDtos = album.getPosts().stream()
                    .map(PostResponseDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setPhotos(photoDtos);
        } else {
            dto.setPhotos(List.of());
        }

        return dto;
    }
}
