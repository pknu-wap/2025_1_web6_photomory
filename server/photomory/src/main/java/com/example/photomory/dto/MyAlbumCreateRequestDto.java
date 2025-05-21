package com.example.photomory.dto;

public class MyAlbumCreateRequestDto {

    private String groupName;
    private String groupDescription;

    public MyAlbumCreateRequestDto() {}

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
}
