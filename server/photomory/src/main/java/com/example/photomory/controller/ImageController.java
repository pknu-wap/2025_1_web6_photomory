    package com.example.photomory.controller;

    import com.example.photomory.entity.Photo;
    import com.example.photomory.entity.Photo;
    import com.example.photomory.repository.ImageRepository;
    import com.example.photomory.service.S3Service;
    import lombok.RequiredArgsConstructor;
    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.*;

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
                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws IOException {

            List<Map<String, String>> uploadedImages = new ArrayList<>();

            System.out.println("파일 수: " + files.size());

            for (MultipartFile file : files) {
                String photoUrl = s3Service.uploadFile(file);
                Photo photo = Photo.builder()
                        .title(title)
                        .date(date)
                        .photoUrl(photoUrl)
                        .photoMakingTime(LocalDateTime.now()) // Photo 엔티티에 이 필드가 있으므로, 현재 시간으로 초기화
                        .photoName(file.getOriginalFilename()) // 업로드된 파일의 원본 이름도 저장
                        .build();
                imageRepository.save(photo);

                //이 부분이 json으로 프론트에 json으로 응답됨
                Map<String, String> imageData = new HashMap<>();
                imageData.put("title", title);
                imageData.put("date", date.toString());
                imageData.put("photoUrl", photoUrl);
                uploadedImages.add(imageData);

                System.out.println("파일 이름: " + file.getOriginalFilename());
            }

            return ResponseEntity.ok(uploadedImages);
        }

        // ✅ 조회: 특정 title + date 조건으로 조회
        @GetMapping("/search")
        public ResponseEntity<List<Map<String, String>>> getImagesByTitleAndDate(
                @RequestParam("title") String title,
                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

            List<Photo> images = imageRepository.findByTitleAndDate(title, date);

            List<Map<String, String>> response = images.stream().map(image -> {
                Map<String, String> data = new HashMap<>();
                data.put("photoUrl", image.getPhotoUrl());
                data.put("title", image.getTitle());
                data.put("date", image.getDate().toString());
                return data;
            }).toList();

            return ResponseEntity.ok(response);
        }

        // ✅ 전체 이미지 조회 (테스트용 등)
        @GetMapping
        public ResponseEntity<List<Photo>> getAllImages() {
            return ResponseEntity.ok(imageRepository.findAll());
        }

        // ✅ 삭제: 이미지 URL로 삭제
        @DeleteMapping("/delete")
        public ResponseEntity<Map<String, String>> deleteImage(@RequestParam("url") String photoUrl) {
            s3Service.deleteFile(photoUrl);
            imageRepository.deleteByPhotoUrl(photoUrl);

            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제 완료");
            return ResponseEntity.ok(response);
        }
    }
