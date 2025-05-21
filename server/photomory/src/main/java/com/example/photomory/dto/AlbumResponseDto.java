package com.example.photomory.dto;

import com.example.photomory.entity.Album;

import java.time.LocalDateTime;

public class AlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumTag;
    private LocalDateTime albumMakingTime;
    private String albumDescription;

    public AlbumResponseDto() {}

    public Integer getAlbumId() {
        return albumId;
    }
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public String getAlbumTag() {
        return albumTag;
    }
    public void setAlbumTag(String albumTag) {
        this.albumTag = albumTag;
    }
    public LocalDateTime getAlbumMakingTime() {
        return albumMakingTime;
    }
    public void setAlbumMakingTime(LocalDateTime albumMakingTime) {
        this.albumMakingTime = albumMakingTime;
    }
    public String getAlbumDescription() {
        return albumDescription;
    }
    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }

    // 엔티티 → DTO 변환 메서드
    public static AlbumResponseDto fromEntity(Album album) {
        AlbumResponseDto dto = new AlbumResponseDto();
        dto.setAlbumId(album.getAlbumId() != null ? album.getAlbumId().intValue() : null);
        dto.setAlbumName(album.getAlbumName());
        dto.setAlbumTag(album.getAlbumTag());
        dto.setAlbumMakingTime(album.getAlbumMakingTime());
        dto.setAlbumDescription(album.getAlbumDescription());
        return dto;
    }
}
