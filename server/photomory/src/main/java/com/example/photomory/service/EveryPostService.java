package com.example.photomory.service;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    public List<EveryPostResponseDto> getAllPostsWithComments() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            EveryPostResponseDto dto = new EveryPostResponseDto();
            dto.setPostId(post.getPostId());

            if (post.getUser() != null) {
                dto.setUserId(post.getUser().getUserId());
                dto.setUserName(post.getUser().getUserName());
                dto.setUserPhotourl(post.getUser().getUserPhotourl());
            }

            dto.setPostText(post.getPostText());
            dto.setPostDescription(post.getPostDescription());
            dto.setLikesCount(post.getLikesCount());
            dto.setLocation(post.getLocation());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setPhotoUrl(post.getPhotoUrl());

            if (post.getTags() != null) {
                dto.setTags(post.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()));
            }

            List<EveryCommentDto> commentDtos = commentRepository.findByPost_PostId(post.getPostId().longValue())
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

        Post post = Post.builder()
                .user(user)
                .postText(dto.getPostText())
                .postDescription(dto.getPostDescription())
                .location(dto.getLocation())
                .likesCount(0)
                .photoUrl(photoUrl)
                .makingTime(time)
                .build();
        postRepository.save(post);

        Photo photo = Photo.builder()
                .post(post)
                .userId(user.getUserId())
                .photoUrl(photoUrl)
                .photoName(dto.getPhotoName())
                .photoComment(dto.getPhotoComment())
                .photoMakingTime(time)
                .date(time.toLocalDate())
                .title(dto.getPhotoName())
                .build();
        photoRepository.save(photo);

        for (String tagName : dto.getTags()) {
            Tag tag = Tag.builder()
                    .tagName(tagName)
                    .posts(Collections.singleton(post))
                    .build();
            tagRepository.save(tag);
        }
    }
}
