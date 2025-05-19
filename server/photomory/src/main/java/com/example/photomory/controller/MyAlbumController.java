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


    @PostMapping("/create")
    public ResponseEntity<MyAlbumDetailDto> createMyAlbum(
            @RequestParam Long userId,
            @RequestParam String myalbumName,
            @RequestParam String myalbumDescription,
            @RequestParam("photos") List<MultipartFile> photoFiles
    ) throws IOException {
        return ResponseEntity.ok(myAlbumService.createMyAlbum(userId, myalbumName, myalbumDescription, photoFiles));
    }

    @GetMapping("/{myalbumId}")
    public ResponseEntity<MyAlbumDetailDto> getMyAlbumDetail(@PathVariable Long myalbumId) {
        return ResponseEntity.ok(myAlbumService.getMyAlbum(myalbumId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<MyAlbumDetailDto> getMyAlbumByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(myAlbumService.getMyAlbumByUserId(userId));
    }
}
