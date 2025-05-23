package com.example.photomory.service;

import com.example.photomory.dto.FriendResponse;
import com.example.photomory.entity.Friend;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.photomory.entity.UserEntity;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    public void deleteFriend(Long fromUserId, Long toUserId) {
        Friend friend = friendRepository.findByFromUserIdAndToUserId(fromUserId, toUserId)
                .orElseThrow(() -> new RuntimeException("친구 관계가 존재하지 않습니다."));
        friendRepository.delete(friend);
    }

    public List<FriendResponse> searchUsers(String keyword) {
        List<UserEntity> userEntities = userRepository.findByUserNameContaining(keyword);
        return userEntities.stream()
                .map(u -> new FriendResponse((long) u.getUserId(), u.getUserName(), u.getUserPhotourl()))
                .collect(Collectors.toList());
    }
}
