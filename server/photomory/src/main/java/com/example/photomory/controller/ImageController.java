package com.example.photomory.controller;

import com.example.photomory.entity.ImageEntity;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins="*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("🔥 [UPLOAD] 파일 수신: " + file);
            System.out.println("📎 파일 이름: " + file.getOriginalFilename());

            String imageUrl = s3Service.uploadFile(file);
            System.out.println("✅ 업로드 성공 URL: " + imageUrl);

            imageRepository.save(new ImageEntity(imageUrl));
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            System.err.println("❌ 업로드 중 예외 발생: " + e.getMessage());
            e.printStackTrace();  // 📛 실제 에러 라인 여기서 찍힘!
            return ResponseEntity.status(500).body("서버 내부 오류: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("url") String imageUrl) {
        s3Service.deleteFile(imageUrl);
        imageRepository.deleteByImageUrl(imageUrl);
        return ResponseEntity.ok("삭제 완료");
    }
}
