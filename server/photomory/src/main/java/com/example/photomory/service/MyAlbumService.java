package com.example.photomory.service;

import com.example.photomory.dto.MyAlbumDetailDto;
import com.example.photomory.dto.MyPhotoDto;
import com.example.photomory.entity.MyAlbum;
import com.example.photomory.entity.MyPhoto;
import com.example.photomory.repository.MyAlbumRepository;
import com.example.photomory.repository.MyPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyAlbumService {

    private final MyAlbumRepository myAlbumRepository;
    private final MyPhotoRepository myPhotoRepository;
    private final S3Service s3Service;

    // 앨범 생성
    public MyAlbumDetailDto createMyAlbum(Long userId, String myalbumName, String myalbumDescription, List<MultipartFile> photos, List<String> mytags) throws IOException {
        MyAlbum album = new MyAlbum();
        album.setUserId(userId);
        album.setMyalbumName(myalbumName);
        album.setMyalbumDescription(myalbumDescription);
        album.setMyalbumMakingtime(LocalDateTime.now());

        // 태그를 ","로 이어붙여 저장
        String tagString = String.join(",", mytags);
        album.setMyalbumTag(tagString);

        MyAlbum saved = myAlbumRepository.save(album);

        List<MyPhotoDto> photoDtos = new ArrayList<>();
        for (MultipartFile file : photos) {
            String url = s3Service.uploadFile(file);

            MyPhoto photo = new MyPhoto();
            photo.setMyalbum(saved);
            photo.setMyphotoUrl(url);
            photo.setMyphotoName(file.getOriginalFilename());
            photo.setMycomment("");
            photo.setMyphotoMakingtime(LocalDateTime.now());
            myPhotoRepository.save(photo);

            photoDtos.add(MyPhotoDto.builder()
                    .myphotoId(photo.getMyphotoId().longValue())
                    .myphotoUrl(photo.getMyphotoUrl())
                    .myphotoName(photo.getMyphotoName())
                    .mycomment(photo.getMycomment())
                    .myphotoMakingtime(photo.getMyphotoMakingtime())
                    .build());
        }

        return MyAlbumDetailDto.builder()
                .myalbumId(saved.getMyalbumId().longValue())
                .userId(saved.getUserId().longValue())
                .myalbumName(saved.getMyalbumName())
                .myalbumDescription(saved.getMyalbumDescription())
                .myalbumMakingtime(saved.getMyalbumMakingtime())
                .myphotos(photoDtos)
                .mytags(mytags)
                .build();
    }

    // 앨범 조회
    public MyAlbumDetailDto getMyAlbum(Long myalbumId) {
        MyAlbum album = myAlbumRepository.findById(myalbumId)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));

        List<MyPhoto> photoList = myPhotoRepository.findByMyalbum(album);
        List<MyPhotoDto> photoDtos = photoList.stream().map(photo -> MyPhotoDto.builder()
                .myphotoId(photo.getMyphotoId().longValue())
                .myphotoUrl(photo.getMyphotoUrl())
                .myphotoName(photo.getMyphotoName())
                .mycomment(photo.getMycomment())
                .myphotoMakingtime(photo.getMyphotoMakingtime())
                .build()).collect(Collectors.toList());

        // 태그 문자열 → 리스트로 변환
        List<String> mytags = new ArrayList<>();
        if (album.getMyalbumTag() != null && !album.getMyalbumTag().isBlank()) {
            mytags = Arrays.asList(album.getMyalbumTag().split(","));
        }

        return MyAlbumDetailDto.builder()
                .myalbumId(album.getMyalbumId().longValue())
                .userId(album.getUserId().longValue())
                .myalbumName(album.getMyalbumName())
                .myalbumDescription(album.getMyalbumDescription())
                .myalbumMakingtime(album.getMyalbumMakingtime())
                .myphotos(photoDtos)
                .mytags(mytags)
                .build();
    }
}
