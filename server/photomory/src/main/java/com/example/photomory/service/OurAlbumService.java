package com.example.photomory.service;

import com.example.photomory.dto.OurAlbumResponseDto;
import com.example.photomory.entity.Album;
import com.example.photomory.repository.AlbumMembersRepository;
import com.example.photomory.repository.AlbumRepository;
import com.example.photomory.repository.FriendRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OurAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;

    public OurAlbumService(AlbumRepository albumRepository,
                           AlbumMembersRepository albumMembersRepository,
                           FriendRepository friendRepository) {
        this.albumRepository = albumRepository;
        this.albumMembersRepository = albumMembersRepository;
        this.friendRepository = friendRepository;
    }

    @Transactional(readOnly = true)
    public OurAlbumResponseDto getAlbumDetails(Integer albumId, Integer requestUserId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        Integer albumOwnerUserId = album.getPost().getUser().getUserId();

        // 1. 요청 유저와 앨범 올린 유저가 친구인지 확인
        boolean areFriends = friendRepository.existsByFromUserIdAndToUserId(requestUserId.longValue(), albumOwnerUserId.longValue());
        if (!areFriends) {
            throw new SecurityException("친구가 아니면 접근할 수 없습니다.");
        }

        // 2. 요청 유저가 앨범 그룹 멤버인지 확인
        boolean isGroupMember = albumMembersRepository.existsByUserEntityUserIdAndMyAlbumMyalbumId(requestUserId, album.getMyAlbum().getMyalbumId());
        if (!isGroupMember) {
            throw new SecurityException("그룹 멤버만 접근할 수 있습니다.");
        }

        return OurAlbumResponseDto.fromEntity(album);
    }
}
