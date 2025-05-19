package com.example.photomory.controller;

import com.example.photomory.entity.ImageEntity;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    //2차-여러장 받고 제목 날짜 같이 받게 다 고침
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    // ✅ 업로드: 한 번에 한 장 또는 여러 장 (title + date 1세트)
    @PostMapping("/upload")
    public ResponseEntity<List<Map<String, String>>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("title") String title,
            @RequestParam("date") String date) throws IOException {

        List<Map<String, String>> uploadedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            String imageUrl = s3Service.uploadFile(file);
            imageRepository.save(new ImageEntity(title, date, imageUrl));

            //이 부분이 json으로 프론트에 json으로 응답됨
            Map<String, String> imageData = new HashMap<>();
            imageData.put("title", title);
            imageData.put("date", date);
            imageData.put("imageUrl", imageUrl);
            uploadedImages.add(imageData);
        }

        return ResponseEntity.ok(uploadedImages);
    }

    // ✅ 조회: 특정 title + date 조건으로 조회
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, String>>> getImagesByTitleAndDate(
            @RequestParam("title") String title,
            @RequestParam("date") String date) {

        List<ImageEntity> images = imageRepository.findByTitleAndDate(title, date);

        List<Map<String, String>> response = images.stream().map(image -> {
            Map<String, String> data = new HashMap<>();
            data.put("imageUrl", image.getImageUrl());
            data.put("title", image.getTitle());
            data.put("date", image.getDate());
            return data;
        }).toList();

        return ResponseEntity.ok(response);
    }

    // ✅ 전체 이미지 조회 (테스트용 등)
    @GetMapping
    public ResponseEntity<List<ImageEntity>> getAllImages() {
        return ResponseEntity.ok(imageRepository.findAll());
    }

    // ✅ 삭제: 이미지 URL로 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteImage(@RequestParam("url") String imageUrl) {
        s3Service.deleteFile(imageUrl);
        imageRepository.deleteByImageUrl(imageUrl);

        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제 완료");
        return ResponseEntity.ok(response);
    }
}
