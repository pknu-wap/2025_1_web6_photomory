package com.example.photomory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class NonFriendUserDto {
    private Long userId;
    private String userName;
    private String userJob;
    private String userPhotourl;
}
