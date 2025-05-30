package com.example.photomory.dto;

import java.util.List;
import com.example.photomory.entity.OurAlbum;


public class OurAlbumFullResponseDto {

    private Integer albumId;
    private String albumTitle;
    private List<PostWithCommentsResponseDto> posts;

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public List<PostWithCommentsResponseDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostWithCommentsResponseDto> posts) {
        this.posts = posts;
    }

    public static OurAlbumFullResponseDto from(OurAlbum album, List<PostWithCommentsResponseDto> posts) {
        OurAlbumFullResponseDto dto = new OurAlbumFullResponseDto();
        dto.setAlbumId(album.getAlbumId());  // Integer 타입이므로 intValue() 생략 가능
        dto.setAlbumTitle(album.getAlbumName());
        dto.setPosts(posts);
        return dto;
    }
}
