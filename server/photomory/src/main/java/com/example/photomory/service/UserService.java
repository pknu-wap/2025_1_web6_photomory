package com.example.photomory.service;

import com.example.photomory.dto.FriendResponse;
import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.User;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);
        List<FriendResponse> friendList = friends.stream()
                .map(f -> userRepository.findById(f.getTo_user_id()).orElse(null))
                .filter(u -> u != null)
                .map(u -> new FriendResponse(u.getUser_id(), u.getUser_name(), u.getPhotourl()))
                .collect(Collectors.toList());

        return new UserProfileResponse(
                user.getUser_name(),
                user.getUser_email(),
                user.getUser_field(),
                user.getUser_equipment(),
                user.getUser_introduction(),
                user.getPhotourl(),
                user.getUser_job(),
                friendList
        );
    }
}
