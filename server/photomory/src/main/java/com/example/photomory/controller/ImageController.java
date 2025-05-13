package com.example.photomory.controller;

import com.example.photomory.entity.ImageEntity;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("📥 파일 받음: " + file);
        System.out.println("📎 파일 이름: " + file.getOriginalFilename());

        String imageUrl = s3Service.uploadFile(file);
        System.out.println("✅ 업로드 성공 URL: " + imageUrl);

        imageRepository.save(new ImageEntity(imageUrl));

        // ✅ JSON 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response); // Content-Type: application/json
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteImage(@RequestParam("url") String imageUrl) {
        s3Service.deleteFile(imageUrl);
        imageRepository.deleteByImageUrl(imageUrl);

        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 완료");
        return ResponseEntity.ok(response);
    }
}
