package com.example.photomory.dto;

import com.example.photomory.entity.MyAlbum;
import com.example.photomory.entity.OurAlbum; // OurAlbum 엔티티 임포트
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 포함합니다.
@NoArgsConstructor // 인자 없는 생성자
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
@Builder // 빌더 패턴을 사용하여 객체 생성
public class GroupResponseDto {

    private Long groupId; // Long 타입으로 변경 (엔티티 ID와 일관성 유지)
    private String groupName;
    private String groupDescription;
    private LocalDateTime createdAt;

    // MyAlbum 엔티티로부터 DTO 생성
    public static GroupResponseDto fromEntity(MyAlbum myAlbum) {
        return GroupResponseDto.builder()
                .groupId(myAlbum.getMyalbumId() != null ? myAlbum.getMyalbumId().longValue() : null)
                .groupName(myAlbum.getMyalbumName())
                .groupDescription(myAlbum.getMyalbumDescription())
                .createdAt(myAlbum.getMyalbumMakingtime())
                .build();
    }

    // OurAlbum 엔티티로부터 DTO 생성 (새롭게 추가된 오버로드 메서드)
    public static GroupResponseDto fromEntity(OurAlbum ourAlbum) {
        return GroupResponseDto.builder()
                .groupId(ourAlbum.getAlbumId() != null ? ourAlbum.getAlbumId().longValue() : null)
                .groupName(ourAlbum.getAlbumName())
                .groupDescription(ourAlbum.getAlbumDescription())
                .createdAt(ourAlbum.getAlbumMakingtime())
                .build();
    }
}