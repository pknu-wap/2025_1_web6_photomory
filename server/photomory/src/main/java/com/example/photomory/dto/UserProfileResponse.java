package com.example.photomory.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String name;
    private String email;
    private String field;
    private String equipment;
    private String introduction;
    private String photourl;
    private String job;

    private List<FriendResponse> friends;
}
