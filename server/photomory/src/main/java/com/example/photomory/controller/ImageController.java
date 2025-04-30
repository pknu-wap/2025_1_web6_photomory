package com.example.photomory.controller;

import com.example.photomory.entity.ImageEntity;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (file == null || file.isEmpty()) {
                response.put("status", "fail");
                response.put("message", "file 파라미터가 null이거나 비어있습니다.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            System.out.println("🔥 [UPLOAD] 파일 수신: " + file);
            System.out.println("📎 파일 이름: " + file.getOriginalFilename());

            String imageUrl = s3Service.uploadFile(file);
            System.out.println("✅ 업로드 성공 URL: " + imageUrl);

            imageRepository.save(new ImageEntity(imageUrl));

            response.put("status", "success");
            response.put("imageUrl", imageUrl);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ 업로드 중 예외 발생: " + e.getMessage());
            e.printStackTrace();

            response.put("status", "error");
            response.put("message", "서버 내부 오류 발생");
            response.put("errorDetail", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("url") String imageUrl) {
        s3Service.deleteFile(imageUrl);
        imageRepository.deleteByImageUrl(imageUrl);
        return ResponseEntity.ok("삭제 완료");
    }
}
