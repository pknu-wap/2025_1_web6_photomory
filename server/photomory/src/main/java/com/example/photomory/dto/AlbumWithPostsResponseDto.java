package com.example.photomory.dto;

import com.example.photomory.entity.OurAlbum; // OurAlbum 엔티티 임포트
import com.example.photomory.entity.OurPost; // Post 대신 OurPost 임포트 (가장 중요!)
// import com.example.photomory.entity.Tag; // Tag 엔티티가 직접 DTO에 사용되지 않으면 제거 가능
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;


@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 포함합니다.
@Builder
@AllArgsConstructor
@NoArgsConstructor // Lombok @NoArgsConstructor 추가
public class AlbumWithPostsResponseDto {
    private Integer albumId;
    private String albumName;
    private List<String> albumTags; // 여러 개의 태그를 담을 리스트 필드
    private String albumDescription;
    private String albumMakingTime; // 엔티티 필드명과 일관성 유지 (대소문자 구분)
    private List<PostWithCommentsResponseDto> posts;

    public static AlbumWithPostsResponseDto from(OurAlbum ourAlbum, List<OurPost> posts) {
        List<PostWithCommentsResponseDto> postDtos = posts.stream()
                .map(ourPost -> PostWithCommentsResponseDto.fromEntity(ourPost, new ArrayList<>(ourPost.getComments())))
                .collect(Collectors.toList());

        List<String> tags;
        // OurAlbum 엔티티의 getAlbumTag() 메서드 사용
        if (ourAlbum.getAlbumTag() != null && !ourAlbum.getAlbumTag().isEmpty()) {
            tags = Arrays.stream(ourAlbum.getAlbumTag().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        } else {
            tags = Collections.emptyList();
        }

        return AlbumWithPostsResponseDto.builder()
                .albumId(ourAlbum.getAlbumId()) // OurAlbum 엔티티의 getAlbumId() 사용
                .albumName(ourAlbum.getAlbumName()) // OurAlbum 엔티티의 getAlbumName() 사용
                .albumTags(tags)
                .albumDescription(ourAlbum.getAlbumDescription()) // OurAlbum 엔티티의 getAlbumDescription() 사용
                // OurAlbum 엔티티의 getAlbumMakingTime() 사용 (필드명 대소문자 일치 중요)
                .albumMakingTime(ourAlbum.getAlbumMakingTime() != null
                        ? ourAlbum.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null)
                .posts(postDtos)
                .build();
    }
}