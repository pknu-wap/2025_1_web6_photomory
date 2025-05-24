package com.example.photomory.dto;

import java.time.LocalDateTime;

public class AlbumCreateRequestDto {

    private String albumName;
    private String albumTag;
    private LocalDateTime albumMakingTime;
    private String albumDescription;
    private Long representativePostId; // <-- 추가된 필드

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

    // representativePostId 필드에 대한 getter 및 setter 추가
    public Long getRepresentativePostId() {
        return representativePostId;
    }

    public void setRepresentativePostId(Long representativePostId) {
        this.representativePostId = representativePostId;
    }
}