package com.example.photomory.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponse {
    private Long userId;
    private String name;
    private String photourl;
}
