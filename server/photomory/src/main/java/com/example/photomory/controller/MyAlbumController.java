package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.UserRepository;
import com.example.photomory.security.JwtTokenProvider;
import com.example.photomory.service.MyAlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/my-albums")
@RequiredArgsConstructor
public class MyAlbumController {

    private final MyAlbumService myAlbumService;
    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;

    private Long extractUserIdFromToken(String token) {
        String username = jwtProvider.extractUsername(token.replace("Bearer ", ""));
        UserEntity user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return user.getUserId();
    }

    @GetMapping
    public ResponseEntity<MyAlbumDetailDto> getMyAlbum(@RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromToken(token);
        MyAlbumDetailDto album = myAlbumService.getMyAlbum(userId);
        return ResponseEntity.ok(album);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MyAlbumDetailDto> createMyAlbum(
            @RequestHeader("Authorization") String token,
            @RequestPart("myalbumName") String myalbumName,
            @RequestPart("myalbumDescription") String myalbumDescription,
            @RequestPart("mytags") String mytags,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
    ) throws IOException {
        Long userId = extractUserIdFromToken(token);
        List<String> tagList = Arrays.asList(mytags.split(","));
        MyAlbumDetailDto dto = myAlbumService.createMyAlbum(
                userId, myalbumName, myalbumDescription, photos, tagList
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MyAlbumDetailDto>> getAllMyAlbums(@RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromToken(token);
        List<MyAlbumDetailDto> albums = myAlbumService.getAllMyAlbums(userId);
        return ResponseEntity.ok(albums);
    }

    @PutMapping(value = "/{albumId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyAlbumDetailDto> updateMyAlbum(
            @PathVariable Long albumId,
            @RequestHeader("Authorization") String token,
            @RequestBody MyAlbumUpdateRequest request
    ) {
        Long userId = extractUserIdFromToken(token);
        MyAlbumDetailDto updated = myAlbumService.updateMyAlbum(albumId, userId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<String> deleteMyAlbum(
            @PathVariable Long albumId,
            @RequestHeader("Authorization") String token
    ) {
        Long userId = extractUserIdFromToken(token);
        myAlbumService.deleteMyAlbum(albumId, userId);
        return ResponseEntity.ok("앨범이 삭제되었습니다.");
    }

    @PatchMapping(value = "/{albumId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MyAlbumDetailDto> addPhotos(
            @PathVariable Long albumId,
            @RequestHeader("Authorization") String token,
            @RequestPart("photoData") String photoDataJson,
            @RequestPart("photos") List<MultipartFile> photos
    ) throws IOException {
        Long userId = extractUserIdFromToken(token);

        ObjectMapper mapper = new ObjectMapper();
        MyPhotoUploadRequest[] requests = mapper.readValue(photoDataJson, MyPhotoUploadRequest[].class);

        if (photos.size() != requests.length) {
            throw new IllegalArgumentException("사진 개수와 photoData 수가 일치하지 않습니다.");
        }

        List<MyPhotoUploadRequest> photoList = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            requests[i].setFile(photos.get(i));
            photoList.add(requests[i]);
        }

        MyAlbumDetailDto updated = myAlbumService.addPhotosToAlbum(albumId, userId, photoList);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<String> deletePhoto(
            @PathVariable int photoId,
            @RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromToken(token);
        myAlbumService.deletePhoto(photoId, userId);
        return ResponseEntity.ok("사진이 삭제되었습니다.");
    }

}