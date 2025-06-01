package com.example.photomory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FriendListDto {
    private Long userId;
    private String userName;
    private String userJob;
    private String userPhotoUrl;
}
