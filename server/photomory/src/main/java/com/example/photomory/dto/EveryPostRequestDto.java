package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class EveryPostRequestDto {
    private String postText;
    private String postDescription;
    private String location;

    private MultipartFile photo;
    private String photoMakingTime;

    private String tagsJson; // "#일상#여행" 형식
    private List<String> tags;

    public void parseTags() {
        if (tagsJson != null && !tagsJson.trim().isEmpty()) {
            this.tags = Arrays.stream(tagsJson.split("#"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }

    public String getTagsRaw() {
        return tagsJson;
    }
}
