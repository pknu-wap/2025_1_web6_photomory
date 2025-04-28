package com.example.photomory.controller;

import com.example.photomory.dto.MyAlbumDetailDto;
import com.example.photomory.service.MyAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-albums")
@RequiredArgsConstructor
public class MyAlbumController {

    private final MyAlbumService myAlbumService;

    @GetMapping("/{albumId}/user/{userId}")
    public ResponseEntity<MyAlbumDetailDto> getMyAlbumDetail(
            @PathVariable Long albumId,
            @PathVariable Long userId) {

        MyAlbumDetailDto albumDetail = myAlbumService.getMyAlbum(albumId, userId);
        return ResponseEntity.ok(albumDetail);
    }
}
