package com.example.photomory.controller;

import com.example.photomory.dto.OurAlbumResponseDto;
import com.example.photomory.service.OurAlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;

    public OurAlbumController(OurAlbumService ourAlbumService) {
        this.ourAlbumService = ourAlbumService;
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<OurAlbumResponseDto> getAlbumDetail(@PathVariable Integer albumId) {
        OurAlbumResponseDto response = ourAlbumService.getAlbumDetail(albumId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
