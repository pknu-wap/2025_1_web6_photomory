package com.example.photomory.dto;

import com.example.photomory.entity.MyAlbum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GroupFullInfoResponseDto {
    private Long groupId;
    private String groupName;
    private String groupDescription;
    private List<UserSummaryDto> members;

    public static GroupFullInfoResponseDto from(MyAlbum album, List<UserSummaryDto> members) {
        return GroupFullInfoResponseDto.builder()
                .groupId(album.getMyalbumId().longValue())
                .groupName(album.getMyalbumName())
                .groupDescription(album.getMyalbumDescription())
                .members(members)
                .build();
    }
}
