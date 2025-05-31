package com.example.photomory.service;

import com.example.photomory.dto.FriendResponse;
import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final S3Service s3Service;

    public UserProfileResponse getUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        List<Friend> friends = Optional.ofNullable(friendRepository.findByFromUserIdAndAreWeFriendTrue(userId))
                .orElse(new ArrayList<>());

        List<FriendResponse> friendList = friends.stream()
                .map(f -> userRepository.findById(f.getToUserId()).orElse(null))
                .filter(Objects::nonNull)
                .map(u -> new FriendResponse(u.getUserId(), u.getUserName(), u.getUserPhotourl()))
                .collect(Collectors.toList());

        return new UserProfileResponse(
                userEntity.getUserName(),
                userEntity.getUserEmail(),
                userEntity.getUserField(),
                userEntity.getUserEquipment(),
                userEntity.getUserIntroduction(),
                userEntity.getUserPhotourl(),
                userEntity.getUserJob(),
                userEntity.getUserArea(),
                friendList
        );
    }

    public void updateUserProfile(UserEntity user,
                                  String name,
                                  String introduction,
                                  String job,
                                  String equipment,
                                  String field,
                                  String area,
                                  MultipartFile photofile) {

        user.setUserName(name);
        user.setUserIntroduction(introduction);
        user.setUserJob(job);
        user.setUserEquipment(equipment);
        user.setUserField(field);
        user.setUserArea(area);

        if (photofile != null && !photofile.isEmpty()) {
            try {
                String imageUrl = s3Service.uploadFile(photofile);
                user.setUserPhotourl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("프로필 사진 업로드 실패", e);
            }
        }


        userRepository.save(user);
    }
}
