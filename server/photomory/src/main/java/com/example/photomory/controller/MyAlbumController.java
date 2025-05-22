package com.example.photomory.controller;

import com.example.photomory.dto.MyAlbumDetailDto;
import com.example.photomory.dto.MyAlbumUpdateRequest;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.MyAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/my-albums")
@RequiredArgsConstructor
public class MyAlbumController {

    private final MyAlbumService myAlbumService;

    @GetMapping
    public ResponseEntity<MyAlbumDetailDto> getMyAlbum(@AuthenticationPrincipal UserEntity user) {
        Long userId = Long.valueOf(user.getUserId());
        MyAlbumDetailDto album = myAlbumService.getMyAlbum(userId);
        return ResponseEntity.ok(album);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MyAlbumDetailDto> createMyAlbum(
            @AuthenticationPrincipal UserEntity user,
            @RequestPart("myalbumName") String myalbumName,
            @RequestPart("myalbumDescription") String myalbumDescription,
            @RequestPart("mytags") String mytags,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
    ) throws IOException {
        List<String> tagList = Arrays.asList(mytags.split(","));
        MyAlbumDetailDto dto = myAlbumService.createMyAlbum(
                user, myalbumName, myalbumDescription, photos, tagList
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MyAlbumDetailDto>> getAllMyAlbums(@AuthenticationPrincipal UserEntity user) {
        Long userId = Long.valueOf(user.getUserId());
        List<MyAlbumDetailDto> albums = myAlbumService.getAllMyAlbums(userId);
        return ResponseEntity.ok(albums);
    }

    @PutMapping(value = "/{albumId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyAlbumDetailDto> updateMyAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal UserEntity user,
            @RequestBody MyAlbumUpdateRequest request
    ) {
        MyAlbumDetailDto updated = myAlbumService.updateMyAlbum(albumId, user, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<String> deleteMyAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal UserEntity user
    ) {
        myAlbumService.deleteMyAlbum(albumId, user);
        return ResponseEntity.ok("앨범이 삭제되었습니다.");
    }

    @PatchMapping(value = "/{albumId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MyAlbumDetailDto> addPhotos(
            @PathVariable Long albumId,
            @AuthenticationPrincipal UserEntity user,
            @RequestPart("photos") List<MultipartFile> photos
    ) throws IOException {
        MyAlbumDetailDto updatedAlbum = myAlbumService.addPhotosToAlbum(albumId, user, photos);
        return ResponseEntity.ok(updatedAlbum);
    }

}
