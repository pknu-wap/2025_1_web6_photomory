package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Tag; // Tag 임포트 추가
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors; // Collectors 임포트 추가

import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
public class AlbumWithPostsResponseDto {
    private Integer albumId;
    private String albumName;
    // private String albumTag; // 기존 단일 태그 필드 제거
    private List<String> albumTags; // 여러 개의 태그를 담을 리스트 필드 추가
    private String albumDescription;
    private String albumMakingTime;
    private List<PostWithCommentsResponseDto> posts;

    public static AlbumWithPostsResponseDto from(Album album, List<Post> posts) {
        List<PostWithCommentsResponseDto> postDtos = posts.stream()
                .map(post -> PostWithCommentsResponseDto.fromEntity(post, Collections.emptyList()))
                .toList();

        // Album 엔티티의 getAlbumTags()에서 List<String>으로 변환
        List<String> tags = album.getAlbumTags() != null ?
                album.getAlbumTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()) :
                Collections.emptyList(); // null이 아닌 빈 리스트 반환

        return AlbumWithPostsResponseDto.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getAlbumName())
                // .albumTag(album.getAlbumTag()) // 이 줄 제거
                .albumTags(tags) // 수정된 필드와 로직 사용
                .albumDescription(album.getAlbumDescription())
                .albumMakingTime(album.getAlbumMakingTime() != null
                        ? album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null)
                .posts(postDtos)
                .build();
    }
}