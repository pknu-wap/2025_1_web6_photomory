package com.example.photomory.dto;

import com.example.photomory.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDto {
    private Long userId;
    private String username;
    private String profileImageUrl; // ✅ 이름은 그대로 가져가도 됨

    public static UserSummaryDto fromEntity(UserEntity user) {
        return UserSummaryDto.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .profileImageUrl(user.getUserPhotourl()) // ✅ 필드명이 일치해야 함
                .build();
    }
}
