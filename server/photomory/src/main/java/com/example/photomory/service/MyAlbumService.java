package com.example.photomory.service;

import com.example.photomory.dto.MyAlbumDetailDto;
import com.example.photomory.dto.MyAlbumUpdateRequest;
import com.example.photomory.dto.MyPhotoDto;
import com.example.photomory.entity.MyAlbum;
import com.example.photomory.entity.MyPhoto;
import com.example.photomory.entity.UserEntity;
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

    public MyAlbumDetailDto createMyAlbum(UserEntity user, String myalbumName, String myalbumDescription,
                                          List<MultipartFile> photos, List<String> mytags) throws IOException {

        MyAlbum album = new MyAlbum();
        album.setUserId(user.getUserId());
        album.setMyalbumName(myalbumName);
        album.setMyalbumDescription(myalbumDescription);
        album.setMyalbumMakingtime(LocalDateTime.now());

        String tagString = String.join(",", mytags);
        album.setMyalbumTag(tagString);

        MyAlbum saved = myAlbumRepository.save(album);

        List<MyPhotoDto> photoDtos = new ArrayList<>();
        if (photos != null) {
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

    public MyAlbumDetailDto getMyAlbum(Long myalbumId) {
        MyAlbum album = myAlbumRepository.findById(myalbumId)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));
        return convertToDto(album);
    }

    public List<MyAlbumDetailDto> getAllMyAlbums(Long userId) {
        List<MyAlbum> albums = myAlbumRepository.findByUserId(userId);
        return albums.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MyAlbumDetailDto updateMyAlbum(Long albumId, UserEntity user, MyAlbumUpdateRequest request) {
        MyAlbum album = myAlbumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("앨범이 존재하지 않습니다."));

        if (!album.getUserId().equals(user.getUserId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        album.setMyalbumName(request.getMyalbumName());
        album.setMyalbumDescription(request.getMyalbumDescription());
        album.setMyalbumTag(String.join(",", request.getMytags()));

        myAlbumRepository.save(album);
        return convertToDto(album);
    }

    public void deleteMyAlbum(Long albumId, UserEntity user) {
        MyAlbum album = myAlbumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));

        if (!album.getUserId().equals(user.getUserId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        List<MyPhoto> photos = myPhotoRepository.findByMyalbum(album);

        for (MyPhoto photo : photos) {
            s3Service.deleteFile(photo.getMyphotoUrl());
            myPhotoRepository.delete(photo);
        }

        myAlbumRepository.delete(album);
    }

    private MyAlbumDetailDto convertToDto(MyAlbum album) {
        List<MyPhoto> photoList = myPhotoRepository.findByMyalbum(album);
        List<MyPhotoDto> photoDtos = photoList.stream().map(photo -> MyPhotoDto.builder()
                .myphotoId(photo.getMyphotoId().longValue())
                .myphotoUrl(photo.getMyphotoUrl())
                .myphotoName(photo.getMyphotoName())
                .mycomment(photo.getMycomment())
                .myphotoMakingtime(photo.getMyphotoMakingtime())
                .build()).collect(Collectors.toList());

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
    public MyAlbumDetailDto addPhotosToAlbum(Long albumId, UserEntity user, List<MultipartFile> photos) throws IOException {
        MyAlbum album = myAlbumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("앨범이 존재하지 않습니다."));

        if (!album.getUserId().equals(user.getUserId())) {
            throw new RuntimeException("사진 추가 권한이 없습니다.");
        }

        List<MyPhotoDto> newPhotos = new ArrayList<>();
        for (MultipartFile file : photos) {
            String url = s3Service.uploadFile(file);

            MyPhoto photo = new MyPhoto();
            photo.setMyalbum(album);
            photo.setMyphotoUrl(url);
            photo.setMyphotoName(file.getOriginalFilename());
            photo.setMycomment("");
            photo.setMyphotoMakingtime(LocalDateTime.now());

            myPhotoRepository.save(photo);

            newPhotos.add(MyPhotoDto.builder()
                    .myphotoId(photo.getMyphotoId().longValue())
                    .myphotoUrl(photo.getMyphotoUrl())
                    .myphotoName(photo.getMyphotoName())
                    .mycomment(photo.getMycomment())
                    .myphotoMakingtime(photo.getMyphotoMakingtime())
                    .build());
        }

        return convertToDto(album);
    }

}
