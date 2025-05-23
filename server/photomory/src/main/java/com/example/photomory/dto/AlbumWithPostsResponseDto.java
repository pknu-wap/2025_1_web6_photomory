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
import java.util.Arrays;


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
                .collect(Collectors.toList());

        List<String> tags;
        if (album.getAlbumTag() != null && !album.getAlbumTag().isEmpty()) {
            tags = Arrays.stream(album.getAlbumTag().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        } else {
            tags = Collections.emptyList();
        }

        return AlbumWithPostsResponseDto.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getAlbumName())
                .albumTags(tags)  // 콤마로 구분된 문자열을 리스트로 변환해서 전달
                .albumDescription(album.getAlbumDescription())
                .albumMakingTime(album.getAlbumMakingTime() != null
                        ? album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null)
                .posts(postDtos)
                .build();
    }
}