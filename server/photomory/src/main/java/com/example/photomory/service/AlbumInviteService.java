package com.example.photomory.service;

import com.example.photomory.dto.FriendListDto;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.AlbumMembersRepository;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AlbumInviteService {
    private final FriendRepository friendRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private final EntityManager entityManager;


    @Transactional(readOnly = true)
    public List<FriendListDto> findNonMemberFriends(Long groupId, Long loginUserId, String keyword) {
        // 로그인한 사람의 친구 목록 (서로 친구 상태)
        List<Friend> friends = friendRepository.findByUserIdAndAreWeFriendTrue(loginUserId);

        // 친구 중 이미 그룹에 들어간 사람은 제외
        List<Long> memberUserIds = albumMembersRepository.findUserIdsByOurAlbumId(groupId.intValue()); // groupId를 Integer로 캐스팅

        //검색어 포함 + 멤버가 아닌 친구만 걸러내기
        return friends.stream()
                .map(friend -> {
                    Long friendId = friend.getFromUserId().equals(loginUserId)
                            ? friend.getToUserId() : friend.getFromUserId();
                    return userRepository.findById(friendId).orElse(null);
                })
                .filter(user -> user != null && !memberUserIds.contains(user.getUserId()))
                .filter(user -> keyword == null || user.getUserName().contains(keyword))
                .map(user -> new FriendListDto(
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserJob(),
                        user.getUserPhotourl()
                ))
                .toList();
    }

}
