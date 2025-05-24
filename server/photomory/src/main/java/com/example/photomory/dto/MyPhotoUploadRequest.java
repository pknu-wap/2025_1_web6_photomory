package com.example.photomory.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MyPhotoUploadRequest {
    private MultipartFile file;
    private String name;
    private String date;
}