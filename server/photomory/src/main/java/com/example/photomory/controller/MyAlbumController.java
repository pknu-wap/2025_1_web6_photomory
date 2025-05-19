package com.example.photomory.controller;

import com.example.photomory.dto.MyAlbumDetailDto;
import com.example.photomory.service.MyAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/my-albums")
@RequiredArgsConstructor
public class MyAlbumController {

    private final MyAlbumService myAlbumService;

    // 나만의 추억 앨범 생성
    @PostMapping("/create")
    public ResponseEntity<MyAlbumDetailDto> createMyAlbum(
            @RequestParam Long userId,
            @RequestParam String myalbumName,
            @RequestParam String myalbumDescription,
            @RequestParam("photos") List<MultipartFile> photoFiles,
            @RequestParam("mytags") List<String> mytags // 추가된 부분
    ) throws IOException {
        return ResponseEntity.ok(
                myAlbumService.createMyAlbum(userId, myalbumName, myalbumDescription, photoFiles, mytags)
        );
    }

    // 나만의 추억 앨범 단건 조회
    @GetMapping("/{myalbumId}")
    public ResponseEntity<MyAlbumDetailDto> getMyAlbumDetail(@PathVariable Long myalbumId) {
        return ResponseEntity.ok(myAlbumService.getMyAlbum(myalbumId));
    }
}
