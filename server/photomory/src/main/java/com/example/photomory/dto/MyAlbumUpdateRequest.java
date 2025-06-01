package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MyAlbumUpdateRequest {
    private String myalbumName;
    private String myalbumDescription;
    private List<String> mytags;
}
