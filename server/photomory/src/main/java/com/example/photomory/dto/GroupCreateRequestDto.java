package com.example.photomory.dto;

public class GroupCreateRequestDto {

    private String groupName;
    private String groupDescription;

    public GroupCreateRequestDto() {}

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
