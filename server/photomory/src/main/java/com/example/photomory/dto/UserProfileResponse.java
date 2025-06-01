package com.example.photomory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("field")
    private String field;

    @JsonProperty("equipment")
    private String equipment;

    @JsonProperty("introduction")
    private String introduction;

    @JsonProperty("user_photourl")
    private String photourl;

    @JsonProperty("job")
    private String job;

    @JsonProperty("user_area")
    private String userArea;

    @JsonProperty("friends")
    private List<FriendResponse> friends;

}
