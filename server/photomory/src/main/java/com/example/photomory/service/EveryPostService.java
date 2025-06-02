package com.example.photomory.service;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.dto.EveryPostUpdateDto;
import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final EveryPostRepository everyPostRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final LikeRepository likeRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<EveryPostResponseDto> getAllPostsWithComments(String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

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
            dto.setLocation(everyPost.getLocation());
            dto.setCreatedAt(everyPost.getCreatedAt());
            dto.setPhotoUrl(everyPost.getPhotoUrl());

            // 좋아요 수 및 여부
            Long likeCount = likeRepository.countByEveryPost(everyPost);
            dto.setLikesCount(likeCount.intValue());
            boolean isLiked = likeRepository.findByUserAndEveryPost(user, everyPost).isPresent();
            dto.setLiked(isLiked);

            // 태그
            if (everyPost.getTags() != null) {
                dto.setTags(everyPost.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()));
            }

            // 댓글
            List<EveryCommentDto> commentDtos = commentRepository.findByEveryPost_PostId(everyPost.getPostId())
                    .stream()
                    .map(comment -> EveryCommentDto.from(comment, comment.getUser().getUserName()))
                    .collect(Collectors.toList());

            dto.setComments(commentDtos);
            dto.setCommentCount(commentDtos.size());

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public EveryPostResponseDto createPost(EveryPostRequestDto dto, String userEmail) {
        dto.parseTags();

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
                .everyAlbum(null)
                .createdAt(LocalDateTime.now())
                .build();
        everyPostRepository.save(everyPost);

        Photo photo = Photo.builder()
                .postType("EVERY")
                .postId(everyPost.getPostId())
                .photoUrl(photoUrl)
                .photoMakingTime(time.toLocalDate())
                .date(time.toLocalDate())
                .title("사진")
                .everyPost(everyPost)
                .build();
        photo.setUser(user);
        photoRepository.save(photo);

        Set<Tag> tagSet = new HashSet<>();
        for (String tagName : dto.getTags()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElse(Tag.builder()
                            .tagName(tagName)
                            .postType("EVERY")
                            .postId(everyPost.getPostId())
                            .build());
            tag.getEveryPosts().add(everyPost);
            tagRepository.save(tag);
            tagSet.add(tag);
        }
        everyPost.setTags(tagSet);

        return EveryPostResponseDto.builder()
                .postId(everyPost.getPostId())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userPhotourl(user.getUserPhotourl())
                .postText(everyPost.getPostText())
                .postDescription(everyPost.getPostDescription())
                .location(everyPost.getLocation())
                .likesCount(0)
                .isLiked(false)
                .createdAt(everyPost.getCreatedAt())
                .photoUrl(everyPost.getPhotoUrl())
                .tags(dto.getTags())
                .commentCount(0)
                .comments(Collections.emptyList())
                .build();
    }

    @Transactional
    public void updatePost(Integer postId, EveryPostUpdateDto dto, String userEmail) {
        dto.parseTags();

        EveryPost post = everyPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUserEmail().equals(userEmail)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        post.setPostText(dto.getPostText());
        post.setPostDescription(dto.getPostDescription());
        post.setLocation(dto.getLocation());

        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            String photoUrl;
            try {
                photoUrl = s3Service.uploadFile(dto.getPhoto());
            } catch (IOException e) {
                throw new RuntimeException("사진 업로드 실패", e);
            }
            post.setPhotoUrl(photoUrl);
            post.setMakingTime(LocalDateTime.parse(dto.getPhotoMakingTime()));
        }

        post.getTags().clear();

        Set<Tag> newTags = new HashSet<>();
        for (String tagName : dto.getTags()) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElse(Tag.builder()
                            .tagName(tagName)
                            .postType("EVERY")
                            .postId(postId)
                            .build());
            tag.getEveryPosts().add(post);
            tagRepository.save(tag);
            newTags.add(tag);
        }

        post.setTags(newTags);
    }
}
