package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EveryCommentDto {
    private Integer userId;
    private String userName;
    private String userPhotourl;
    private String commentText;
}
