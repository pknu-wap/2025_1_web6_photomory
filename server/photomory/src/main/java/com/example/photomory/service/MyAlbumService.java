package com.example.photomory.service;

import com.example.photomory.dto.*;
import com.example.photomory.entity.*;
import com.example.photomory.repository.MyAlbumRepository;
import com.example.photomory.repository.MyPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
        // Long 타입의 myalbumId를 Integer로 변환합니다. MyAlbum ID는 Integer이기 때문입니다.
        Integer myalbumIdInt = myalbumId.intValue();

        // 수정: MyAlbumRepository는 Integer ID를 기대하므로 myalbumIdInt를 사용합니다.
        MyAlbum album = myAlbumRepository.findById(myalbumIdInt) // myalbumId 대신 myalbumIdInt를 사용합니다.
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

    public MyAlbumDetailDto addPhotosToAlbum(Long albumId, UserEntity user, List<MyPhotoUploadRequest> photos) throws IOException {
        MyAlbum album = myAlbumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("앨범이 존재하지 않습니다."));

        if (!album.getUserId().equals(user.getUserId())) {
            throw new RuntimeException("사진 추가 권한이 없습니다.");
        }

        for (MyPhotoUploadRequest request : photos) {
            String url = s3Service.uploadFile(request.getFile());

            MyPhoto photo = new MyPhoto();
            photo.setMyalbum(album);
            photo.setMyphotoUrl(url);
            photo.setMyphotoName(request.getName());
            photo.setMycomment("");
            photo.setMyphotoMakingtime(LocalDate.parse(request.getDate()).atStartOfDay());

            myPhotoRepository.save(photo);
        }

        return convertToDto(album);
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
                .userId(album.getUserId().longValue()) // User ID는 Long인 것이 맞으므로 이대로 둡니다.
                .myalbumName(album.getMyalbumName())
                .myalbumDescription(album.getMyalbumDescription())
                .myalbumMakingtime(album.getMyalbumMakingtime())
                .myphotos(photoDtos)
                .mytags(mytags)
                .build();
    }
    public void deletePhoto(int photoId, UserEntity user) {
        MyPhoto photo = myPhotoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("사진이 존재하지 않습니다."));

        if (!photo.getMyalbum().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        s3Service.deleteFile(photo.getMyphotoUrl());
        myPhotoRepository.delete(photo);
    }

}
