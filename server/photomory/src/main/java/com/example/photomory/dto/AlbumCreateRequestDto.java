package com.example.photomory.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // 기본 생성자를 위한 Lombok 어노테이션
import lombok.AllArgsConstructor; // 모든 필드를 포함하는 생성자를 위한 Lombok 어노테이션 (선택 사항)
import lombok.Builder; // 빌더 패턴을 사용한다면 추가 (선택 사항)

@Getter // Lombok이 모든 getter 메서드를 자동 생성합니다.
@Setter // Lombok이 모든 setter 메서드를 자동 생성합니다.
@NoArgsConstructor // Lombok이 기본 생성자를 자동 생성합니다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 (필요에 따라 추가)
@Builder // 빌더 패턴을 사용한다면 추가 (필요에 따라 추가)
public class AlbumCreateRequestDto {

    private String albumName;
    private String albumTag;
    private LocalDateTime albumMakingTime;
    private String albumDescription;


}