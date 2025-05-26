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
    private int friendId;  // int 타입으로 변경

    public static UserSummaryDto frmEntity(UserEntity user, Integer friendId) {
        return UserSummaryDto.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .friendId(friendId)  // 새 필드 포함
                .build();
    }

}
