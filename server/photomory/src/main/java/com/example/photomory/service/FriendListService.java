package com.example.photomory.service;

import com.example.photomory.dto.FriendListDto;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.AlbumMembersRepository;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FriendListService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final AlbumMembersRepository albumMembersRepository;

    public List<FriendListDto> getFriendList(Long loginUserId) {
        List<Friend> friends = friendRepository.findByUserIdAndAreWeFriendTrue(loginUserId);

        // 중복 제거를 위해 Set으로 먼저 friendId만 추출!
        Set<Long> uniqueFriendIds = friends.stream()
                .map(friend -> friend.getFromUserId().equals(loginUserId)
                        ? friend.getToUserId()
                        : friend.getFromUserId())
                .collect(Collectors.toSet());

        // 이제 중복 없는 friendId로 친구 정보 조회
        return uniqueFriendIds.stream()
                .map(friendId -> {
                    UserEntity friendUser = userRepository.findById(friendId)
                            .orElseThrow(() -> new RuntimeException("친구 정보가 존재하지 않습니다."));
                    return new FriendListDto(
                            friendUser.getUserId(),
                            friendUser.getUserName(),
                            friendUser.getUserJob(),
                            friendUser.getUserPhotourl()
                    );
                })
                .toList();

    }

    @Transactional
    public void deleteMyFriend(Long loginUserId, Long friendUserId) {
        friendRepository.deleteByFromUserIdAndToUserId(loginUserId, friendUserId);
        friendRepository.deleteByFromUserIdAndToUserId(friendUserId, loginUserId);
    }

    //그룹추가에 쓰는 친구리스트
    @Transactional(readOnly = true)
    public List<FriendListDto> findNonMemberFriends(Long groupId, Long loginUserId, String keyword) {
        // 친구 목록 조회
        List<Friend> friends = friendRepository.findByUserIdAndAreWeFriendTrue(loginUserId);

        // 이미 그룹에 속한 사용자 ID들 조회
        List<Long> memberUserIds = albumMembersRepository.findUserIdsByMyAlbumId(groupId);

        // 친구 중에, 그룹 멤버가 아닌 친구만 필터링
        return friends.stream()
                .map(friend -> {
                    Long friendId = friend.getFromUserId().equals(loginUserId)
                            ? friend.getToUserId()
                            : friend.getFromUserId();

                    return userRepository.findById(friendId)
                            .orElseThrow(() -> new RuntimeException("친구 정보가 존재하지 않습니다."));
                })
                .filter(user -> !memberUserIds.contains(user.getUserId())) // 그룹 멤버 제외
                .filter(user -> keyword == null || user.getUserName().contains(keyword)) // 검색어 필터
                .map(user -> new FriendListDto(
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserJob(),
                        user.getUserPhotourl()
                ))
                .toList();
    }
}
