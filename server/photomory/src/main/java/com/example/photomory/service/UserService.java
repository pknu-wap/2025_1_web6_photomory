package com.example.photomory.service;

import com.example.photomory.dto.FriendResponse;
import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.entity.Friend;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.photomory.entity.UserEntity;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    public UserProfileResponse getUserProfile(Long userId) {
        // 유저 정보 조회
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        List<Friend> friends = Optional.ofNullable(friendRepository.findByFromUserIdAndAreWeFriendTrue(userId))
                .orElse(new ArrayList<>());

        List<FriendResponse> friendList = friends.stream()
                .map(f -> userRepository.findById(f.getToUserId()).orElse(null)) // toUserId로 유저 조회
                .filter(u -> u != null) // null 걸러주기
                .map(u -> new FriendResponse((long) u.getUserId(), u.getUserName(), u.getUserPhotourl()))
                .collect(Collectors.toList());


        return new UserProfileResponse(
                userEntity.getUserName(),
                userEntity.getUserEmail(),
                userEntity.getUserField(),
                userEntity.getUserEquipment(),
                userEntity.getUserIntroduction(),
                userEntity.getUserPhotourl(),
                userEntity.getUserJob(),
                friendList
        );
    }
}