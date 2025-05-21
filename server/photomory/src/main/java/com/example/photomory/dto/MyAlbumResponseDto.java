package com.example.photomory.dto;

import com.example.photomory.entity.MyAlbum;

import java.time.LocalDateTime;

public class MyAlbumResponseDto {

    private Integer myAlbumId;
    private String groupName;
    private String groupDescription;
    private LocalDateTime createdAt;

    public MyAlbumResponseDto() {}

    public Integer getMyAlbumId() {
        return myAlbumId;
    }

    public void setMyAlbumId(Integer myAlbumId) {
        this.myAlbumId = myAlbumId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static MyAlbumResponseDto fromEntity(MyAlbum myAlbum) {
        MyAlbumResponseDto dto = new MyAlbumResponseDto();
        dto.setMyAlbumId(myAlbum.getMyalbumId());
        dto.setGroupName(myAlbum.getMyalbumName());          // myalbumName 필드 매칭
        dto.setGroupDescription(myAlbum.getMyalbumDescription());  // myalbumDescription 필드 매칭
        dto.setCreatedAt(myAlbum.getMyalbumMakingtime());     // myalbumMakingtime 필드 매칭
        return dto;
    }
}
