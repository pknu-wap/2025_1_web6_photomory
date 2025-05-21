package com.example.photomory.dto;

import com.example.photomory.entity.MyAlbum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoResponseDto {
    private Long groupId;
    private String groupName;
    private String groupDescription;
    private Long ownerId;

    public static GroupInfoResponseDto fromEntity(MyAlbum myAlbum) {
        return GroupInfoResponseDto.builder()
                .groupId(myAlbum.getMyalbumId())
                .groupName(myAlbum.getMyalbumName())
                .groupDescription(myAlbum.getMyalbumDescription())
                .ownerId(myAlbum.getUserId())
                .build();
    }
}
