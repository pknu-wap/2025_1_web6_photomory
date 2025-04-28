package com.example.photomory.controller;

import com.example.photomory.entity.ImageEntity;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = s3Service.uploadFile(file);
        imageRepository.save(new ImageEntity(imageUrl));
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("url") String imageUrl) {
        s3Service.deleteFile(imageUrl);
        imageRepository.deleteByImageUrl(imageUrl);
        return ResponseEntity.ok("삭제 완료");
    }
}
