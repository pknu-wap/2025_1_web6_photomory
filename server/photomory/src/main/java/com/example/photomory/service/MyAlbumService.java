package com.example.photomory.service;
import com.example.photomory.dto.MyAlbumDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.photomory.repository.AlbumRepository;
import com.example.photomory.dto.MyAlbumDetailDto;
import com.example.photomory.dto.MyPhotoDto;
import com.example.photomory.entity.Album;


@Service
@RequiredArgsConstructor
public class MyAlbumService {

    private final AlbumRepository albumRepository;

    public MyAlbumDetailDto getMyAlbum(Long albumId, Long userId) {
        Album album = albumRepository.findMyAlbum(albumId, userId)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));

        List<MyPhotoDto> photoDtos = album.getPhotos().stream()
                .map(photo -> MyPhotoDto.builder()
                        .photoId(photo.getPhotoId())
                        .photoUrl(photo.getPhotoUrl())
                        .photoName(photo.getPhotoName())
                        .comment(photo.getPhotoComment())
                        .photoMakingTime(photo.getPhotoMakingtime())
                        .build())
                .collect(Collectors.toList());

        List<String> tagNames = album.getTags().stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());

        return MyAlbumDetailDto.builder()
                .albumId(album.getAlbumId())
                .userId(album.getUserId())
                .albumName(album.getAlbumName())
                .albumDescription(album.getAlbumDescription())
                .albumMakingTime(album.getAlbumMakingtime())
                .photos(photoDtos)
                .tags(tagNames)
                .build();
    }
}
