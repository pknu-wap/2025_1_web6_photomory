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
        // 1. 앨범 조회 (없으면 404)
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        // 2. 앨범에 속한 게시물이 하나라도 있는지 확인하고 소유자 아이디 추출
        if (album.getPosts() == null || album.getPosts().isEmpty()) {
            throw new EntityNotFoundException("앨범에 게시물이 없습니다.");
        }
        Integer albumOwnerUserId = album.getPosts().get(0).getUser().getUserId();

        // 3. 요청 유저와 앨범 소유자가 친구인지 확인 (없으면 403)
        boolean areFriends = friendRepository.existsByFromUserIdAndToUserId(
                requestUserId.longValue(), albumOwnerUserId.longValue()
        );
        if (!areFriends) {
            throw new SecurityException("친구가 아니면 접근할 수 없습니다.");
        }

        // 4. 요청 유저가 앨범의 그룹 멤버인지 확인 (없으면 403)
        boolean isGroupMember = albumMembersRepository.existsByUserEntityUserIdAndMyAlbumMyalbumId(
                requestUserId, album.getMyAlbum().getMyalbumId()
        );
        if (!isGroupMember) {
            throw new SecurityException("그룹 멤버만 접근할 수 있습니다.");
        }

        // 5. DTO 변환 후 반환
        return OurAlbumResponseDto.fromEntity(album);
    }
}
