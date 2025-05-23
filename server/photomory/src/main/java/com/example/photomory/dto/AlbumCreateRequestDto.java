package com.example.photomory.dto;

import java.time.LocalDateTime;

public class AlbumCreateRequestDto {

    private String albumName;
    private String albumTag;
    private LocalDateTime albumMakingTime;
    private String albumDescription;

    public AlbumCreateRequestDto() {}

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
}
