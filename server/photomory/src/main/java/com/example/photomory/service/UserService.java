package com.example.photomory.service;

import com.example.photomory.dto.FriendResponse;
import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public UserProfileResponse getUserProfile(UserEntity userEntity) {
        Long userId = (long) userEntity.getUserId();  // int → long 변환

        List<Friend> friends = Optional.ofNullable(friendRepository.findByFromUserIdAndAreWeFriendTrue(userId))
                .orElse(new ArrayList<>());

        List<FriendResponse> friendList = friends.stream()
                .map(f -> userRepository.findById(f.getToUserId()).orElse(null))
                .filter(u -> u != null)
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
