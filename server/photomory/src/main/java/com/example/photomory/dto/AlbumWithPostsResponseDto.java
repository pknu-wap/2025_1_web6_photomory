package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Collections;


import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AlbumWithPostsResponseDto {
    private Integer albumId;
    private String albumName;
    private String albumTag;
    private String albumDescription;
    private String albumMakingTime;
    private List<PostWithCommentsResponseDto> posts;

    public static AlbumWithPostsResponseDto from(Album album, List<Post> posts) {
        List<PostWithCommentsResponseDto> postDtos = posts.stream()
                .map(post -> PostWithCommentsResponseDto.fromEntity(post, Collections.emptyList()))
                .toList();

        return AlbumWithPostsResponseDto.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getAlbumName())
                .albumTag(album.getAlbumTag())
                .albumDescription(album.getAlbumDescription())
                .albumMakingTime(album.getAlbumMakingTime() != null
                        ? album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null)
                .posts(postDtos)
                .build();
    }
}
