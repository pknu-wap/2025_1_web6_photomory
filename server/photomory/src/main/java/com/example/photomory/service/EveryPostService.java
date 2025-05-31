package com.example.photomory.service;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final EveryPostRepository everyPostRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<EveryPostResponseDto> getAllPostsWithComments() {
        List<EveryPost> everyPosts = everyPostRepository.findAll();

        return everyPosts.stream().map(everyPost -> {
            EveryPostResponseDto dto = new EveryPostResponseDto();
            dto.setPostId(everyPost.getPostId());

            if (everyPost.getUser() != null) {
                dto.setUserId(everyPost.getUser().getUserId());
                dto.setUserName(everyPost.getUser().getUserName());
                dto.setUserPhotourl(everyPost.getUser().getUserPhotourl());
            }

            dto.setPostText(everyPost.getPostText());
            dto.setPostDescription(everyPost.getPostDescription());
            dto.setLikesCount(everyPost.getLikesCount());
            dto.setLocation(everyPost.getLocation());
            dto.setCreatedAt(everyPost.getCreatedAt());
            dto.setPhotoUrl(everyPost.getPhotoUrl());

            if (everyPost.getTags() != null) {
                dto.setTags(everyPost.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()));
            }

            List<EveryCommentDto> commentDtos = commentRepository.findByEveryPost_PostId(everyPost.getPostId())
                    .stream()
                    .map(comment -> {
                        UserEntity commenter = comment.getUser();
                        return EveryCommentDto.builder()
                                .userId(commenter.getUserId())
                                .userName(commenter.getUserName())
                                .userPhotourl(commenter.getUserPhotourl())
                                .comment(comment.getCommentText())
                                .createdAt(comment.getCommentTime())
                                .build();
                    }).collect(Collectors.toList());

            dto.setComments(commentDtos);
            dto.setCommentCount(commentDtos.size());

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void createPost(EveryPostRequestDto dto, String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        String photoUrl;
        try {
            photoUrl = s3Service.uploadFile(dto.getPhoto());
        } catch (IOException e) {
            throw new RuntimeException("사진 업로드 실패", e);
        }

        LocalDateTime time = LocalDateTime.parse(dto.getPhotoMakingTime());

        EveryPost everyPost = EveryPost.builder()
                .user(user)
                .postText(dto.getPostText())
                .postDescription(dto.getPostDescription())
                .location(dto.getLocation())
                .likesCount(0)
                .photoUrl(photoUrl)
                .makingTime(time)
                .createdAt(LocalDateTime.now())
                .build();
        everyPostRepository.save(everyPost);

        Photo photo = Photo.builder()
                .everyPost(everyPost)
                .photoUrl(photoUrl)
                .photoName(dto.getPhotoName())
                .photoMakingTime(time)
                .date(time.toLocalDate())
                .title(dto.getPhotoName())
                .build();
        photoRepository.save(photo);

        for (String tagName : dto.getTags()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElse(Tag.builder().tagName(tagName).build());

            Set<EveryPost> everyPostsForTag = tag.getEveryPosts();
            if (everyPostsForTag == null) {
                everyPostsForTag = new HashSet<>();
                tag.setEveryPosts(everyPostsForTag);
            }
            everyPostsForTag.add(everyPost);

            tagRepository.save(tag);
        }
    }
}