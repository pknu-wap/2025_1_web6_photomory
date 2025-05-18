package com.example.photomory.service;

import com.example.photomory.dto.OurAlbumResponseDto;
import com.example.photomory.entity.Album;
import com.example.photomory.repository.AlbumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OurAlbumService {

    private final AlbumRepository albumRepository;

    public OurAlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public OurAlbumResponseDto getAlbumDetail(Integer albumId) {
        Optional<Album> albumOpt = albumRepository.findById(albumId);
        if (albumOpt.isPresent()) {
            return OurAlbumResponseDto.fromEntity(albumOpt.get());
        } else {
            // 필요하면 예외 던지거나 null 반환
            return null;
        }
    }
}
