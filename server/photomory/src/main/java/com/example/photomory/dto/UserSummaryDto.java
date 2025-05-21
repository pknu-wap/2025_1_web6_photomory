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
    private String profileImageUrl;

    public static UserSummaryDto fromEntity(UserEntity user) {
        return UserSummaryDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
