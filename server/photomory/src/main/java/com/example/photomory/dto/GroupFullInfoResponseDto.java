package com.example.photomory.dto;

import com.example.photomory.entity.OurAlbum; // MyAlbum 대신 OurAlbum을 임포트합니다.
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor; // @NoArgsConstructor 추가: 빌더 사용 시 필요

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor // Lombok의 @Builder를 사용하려면 @NoArgsConstructor가 필요합니다.
public class GroupFullInfoResponseDto {
    private Long groupId;          // 기존 필드명 유지
    private String groupName;      // 기존 필드명 유지
    private String groupDescription; // 기존 필드명 유지
    private List<UserSummaryDto> members; // 기존 필드명 유지

    /**
     *
     * @param ourAlbum OurAlbum 엔티티 (MyAlbum 대신 OurAlbum을 받도록 시그니처 변경)
     * @param members  그룹 구성원 요약 DTO 리스트
     * @return GroupFullInfoResponseDto
     */
    public static GroupFullInfoResponseDto from(OurAlbum ourAlbum, List<UserSummaryDto> members) {
        return GroupFullInfoResponseDto.builder()
                .groupId(ourAlbum.getAlbumId() != null ? ourAlbum.getAlbumId().longValue() : null) // OurAlbum의 ID를 Long으로 변환
                .groupName(ourAlbum.getAlbumName()) // OurAlbum의 이름 필드 사용
                .groupDescription(ourAlbum.getAlbumDescription()) // OurAlbum의 설명 필드 사용
                .members(members)
                .build();
    }
}