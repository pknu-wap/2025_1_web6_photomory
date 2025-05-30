package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class EveryPostRequestDto {
    private String postText;
    private String postDescription;
    private String location;

    private MultipartFile photo;
    private String photoName;
    private String photoComment;
    private String photoMakingTime;

    private List<String> tags;

    // Postman에서 문자열로 전달된 JSON 배열을 List로 변환
    public void setTagsJson(String tagsJson) {
        // ["하늘", "일상"] → 리스트로 파싱
        this.tags = Arrays.asList(tagsJson
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .split(","));
    }
}
