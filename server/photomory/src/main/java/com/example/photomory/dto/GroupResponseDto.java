package com.example.photomory.dto;

import com.example.photomory.entity.MyAlbum;

import java.time.LocalDateTime;

public class GroupResponseDto {

    private Integer groupId;
    private String groupName;
    private String groupDescription;
    private LocalDateTime createdAt;

    public GroupResponseDto() {}

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public static GroupResponseDto fromEntity(MyAlbum myAlbum) {
        GroupResponseDto dto = new GroupResponseDto();
        dto.setGroupId(myAlbum.getMyalbumId());
        dto.setGroupName(myAlbum.getMyalbumName());
        dto.setGroupDescription(myAlbum.getMyalbumDescription());
        dto.setCreatedAt(myAlbum.getMyalbumMakingtime());
        return dto;
    }
}
