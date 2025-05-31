package com.example.photomory.dto;

import com.example.photomory.entity.OurAlbum; // Album 대신 OurAlbum 임포트
import com.example.photomory.entity.Photo; // PostResponseDto::fromEntity에서 Photo 엔티티가 필요할 수 있으므로 추가
import lombok.AllArgsConstructor;
import lombok.Builder; // @Builder 어노테이션 추가
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있게 합니다.
public class AlbumResponseDto {

    private Long albumId; // Integer에서 Long으로 변경 (엔티티 ID와 일관성 유지)
    private String albumName;
    private List<String> albumTags;
    private LocalDateTime albumMakingTime;
    private String albumDescription;
    private Long groupId; // Integer에서 Long으로 변경 (엔티티 ID와 일관성 유지)

    private List<PostResponseDto> posts; // 필드명을 photos에서 posts로 변경하여 Post 엔티티와 일관성 유지

    // OurAlbum 엔티티로부터 DTO 생성
    public static AlbumResponseDto fromEntity(OurAlbum ourAlbum) { // Album 대신 OurAlbum 사용
        // Lombok의 @Builder를 사용하여 더 간결하게 DTO를 생성합니다.
        return AlbumResponseDto.builder()
                .albumId(ourAlbum.getAlbumId() != null ? ourAlbum.getAlbumId().longValue() : null)
                .albumName(ourAlbum.getAlbumName())
                .albumDescription(ourAlbum.getAlbumDescription())
                .albumMakingTime(ourAlbum.getAlbumMakingTime()) // 필드명은 소문자 't'에서 대문자 'T'로 통일되어야 합니다.
                .groupId(ourAlbum.getMyAlbum() != null ? ourAlbum.getMyAlbum().getMyalbumId().longValue() : null)
                .posts(ourAlbum.getPosts() != null ? // OurAlbum에 getPosts()가 있다고 가정
                        ourAlbum.getPosts().stream()
                                .map(post -> PostResponseDto.fromEntity(post)) // PostResponseDto::fromEntity를 호출
                                .collect(Collectors.toList()) :
                        List.of())
                .albumTags(ourAlbum.getAlbumTag() != null && !ourAlbum.getAlbumTag().isEmpty() ?
                        Arrays.stream(ourAlbum.getAlbumTag().split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()) :
                        List.of())
                .build();
    }
}