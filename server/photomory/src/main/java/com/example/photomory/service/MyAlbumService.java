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

    public MyAlbumDetailDto createMyAlbum(Long userId, String myalbumName, String myalbumDescription, List<MultipartFile> photos) throws IOException {
        MyAlbum album = new MyAlbum();
        album.setUserId(userId);
        album.setMyalbumName(myalbumName);
        album.setMyalbumDescription(myalbumDescription);
        album.setMyalbumMakingtime(LocalDateTime.now());

        MyAlbum saved = myAlbumRepository.save(album);

        List<MyPhotoDto> photoDtos = new ArrayList<>();
        for (MultipartFile file : photos) {
            String url = s3Service.uploadFile(file);
            MyPhoto photo = new MyPhoto();
            photo.setMyalbum(saved);
            photo.setMyphotoUrl(url);
            myPhotoRepository.save(photo);

            photoDtos.add(MyPhotoDto.builder()
                    .myphotoId(photo.getMyphotoId().longValue())
                    .myphotoUrl(photo.getMyphotoUrl())
                    .myphotoName(file.getOriginalFilename())
                    .mycomment("")
                    .myphotoMakingtime(LocalDateTime.now())
                    .build());
        }

        return MyAlbumDetailDto.builder()
                .myalbumId(saved.getMyalbumId().longValue())
                .userId(saved.getUserId().longValue())
                .myalbumName(saved.getMyalbumName())
                .myalbumDescription(saved.getMyalbumDescription())
                .myalbumMakingtime(saved.getMyalbumMakingtime())
                .myphotos(photoDtos)
                .mytags(saved.getMyalbumTag() != null ?
                        List.of(saved.getMyalbumTag().split(",")) : Collections.emptyList())
                .build();
    }

    public MyAlbumDetailDto getMyAlbum(Long myalbumId) {
        MyAlbum album = myAlbumRepository.findById(myalbumId)
                .orElseThrow(() -> new RuntimeException("앨범이 존재하지 않습니다."));

        List<MyPhotoDto> photoDtos = album.getPhotos().stream()
                .map(p -> MyPhotoDto.builder()
                        .myphotoId(p.getMyphotoId().longValue())
                        .myphotoUrl(p.getMyphotoUrl())
                        .myphotoName("사진")
                        .mycomment("")
                        .myphotoMakingtime(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        return MyAlbumDetailDto.builder()
                .myalbumId(album.getMyalbumId().longValue())
                .userId(album.getUserId().longValue())
                .myalbumName(album.getMyalbumName())
                .myalbumDescription(album.getMyalbumDescription())
                .myalbumMakingtime(album.getMyalbumMakingtime())
                .myphotos(photoDtos)
                .mytags(album.getMyalbumTag() != null ?
                        List.of(album.getMyalbumTag().split(",")) : Collections.emptyList())
                .build();
    }
}
