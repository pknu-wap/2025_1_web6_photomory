package com.example.photomory.dto;

import com.example.photomory.entity.OurPost; // !!! Post 대신 OurPost를 임포트합니다. !!!
import lombok.AllArgsConstructor; // Lombok 어노테이션 추가
import lombok.Builder;             // Lombok 어노테이션 추가
import lombok.Data;                // Lombok 어노테이션 추가 (Getter, Setter 등을 자동 생성)
import lombok.NoArgsConstructor;   // Lombok 어노테이션 추가

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 자동 생성합니다.
@NoArgsConstructor // 인자 없는 기본 생성자를 자동 생성합니다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동 생성합니다. (Builder 사용 시 필요)
@Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있게 합니다.
public class PostResponseDto {
    private Integer postId;
    private String postText;

    /**
     *
     * @param ourPost OurPost 엔티티
     * @return PostResponseDto
     */
    public static PostResponseDto fromEntity(OurPost ourPost) { // !!! Post에서 OurPost로 변경 !!!
        // Lombok의 @Builder를 사용하여 객체를 간결하게 생성합니다.
        return PostResponseDto.builder()
                .postId(ourPost.getPostId())
                .postText(ourPost.getPostText())
                .build();
    }

}