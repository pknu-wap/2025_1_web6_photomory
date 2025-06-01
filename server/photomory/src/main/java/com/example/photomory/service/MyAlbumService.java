package com.example.photomory.service;

import com.example.photomory.dto.*;
import com.example.photomory.entity.*;
import com.example.photomory.repository.MyAlbumRepository;
import com.example.photomory.repository.MyPhotoRepository;
import com.example.photomory.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public MyAlbumDetailDto createMyAlbum(Long userId, String myalbumName, String myalbumDescription,
                                          List<MultipartFile> photos, List<String> mytags) throws IOException {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

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

    public MyAlbumDetailDto getMyAlbum(Long userId) {
        List<MyAlbum> albums = myAlbumRepository.findByUserId(userId);
        if (albums.isEmpty()) {
            throw new RuntimeException("해당 유저의 앨범이 존재하지 않습니다.");
        }
        return convertToDto(albums.get(0)); // 첫 앨범 반환 (필요시 수정)
    }

    public List<MyAlbumDetailDto> getAllMyAlbums(Long userId) {
        List<MyAlbum> albums = myAlbumRepository.findByUserId(userId);
        if (albums == null) {
            albums = new ArrayList<>();
        }
        return albums.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MyAlbumDetailDto updateMyAlbum(Long albumId, Long userId, MyAlbumUpdateRequest request) {
        Integer albumIdInt = albumId.intValue();

        MyAlbum album = myAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new RuntimeException("앨범이 존재하지 않습니다."));

        if (!album.getUserId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        album.setMyalbumName(request.getMyalbumName());
        album.setMyalbumDescription(request.getMyalbumDescription());
        album.setMyalbumTag(String.join(",", request.getMytags()));

        myAlbumRepository.save(album);
        return convertToDto(album);
    }

    public void deleteMyAlbum(Long albumId, Long userId) {
        Integer albumIdInt = albumId.intValue();

        MyAlbum album = myAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));

        if (!album.getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        List<MyPhoto> photos = myPhotoRepository.findByMyalbum(album);

        for (MyPhoto photo : photos) {
            s3Service.deleteFile(photo.getMyphotoUrl());
            myPhotoRepository.delete(photo);
        }

        myAlbumRepository.delete(album);
    }

    public MyAlbumDetailDto addPhotosToAlbum(Long albumId, Long userId, List<MyPhotoUploadRequest> photos) throws IOException {
        Integer albumIdInt = albumId.intValue();

        MyAlbum album = myAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new RuntimeException("앨범이 존재하지 않습니다."));

        if (!album.getUserId().equals(userId)) {
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
                .userId(album.getUserId().longValue())
                .myalbumName(album.getMyalbumName())
                .myalbumDescription(album.getMyalbumDescription())
                .myalbumMakingtime(album.getMyalbumMakingtime())
                .myphotos(photoDtos)
                .mytags(mytags)
                .build();
    }

    public void deletePhoto(int photoId, Long userId) {
        MyPhoto photo = myPhotoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("사진이 존재하지 않습니다."));

        if (!photo.getMyalbum().getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        s3Service.deleteFile(photo.getMyphotoUrl());
        myPhotoRepository.delete(photo);
    }
}
