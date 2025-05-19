package com.example.photomory.service;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.entity.Photo;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Tag;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.PhotoRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.TagRepository;
import com.example.photomory.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final PostRepository postRepository;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createPost(EveryPostRequestDto dto) {
        // 1. 유저 조회
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 게시글 저장
        Post post = new Post();
        post.setUser(user);
        post.setPostText(dto.getPostText());
        post.setPostDescription(dto.getPostDescription());
        post.setLocation(dto.getLocation());
        post.setLikesCount(0);
        Post savedPost = postRepository.save(post);

        // 3. 사진 저장 (S3 URL 포함)
        Photo photo = new Photo();
        photo.setPhotoUrl(dto.getPhotoUrl());
        photo.setPhotoName(dto.getPhotoName());
        photo.setPhotoComment(dto.getPhotoComment());
        photo.setPhotoMakingTime(dto.getPhotoMakingTime());
        photo.setPost(savedPost);
        photoRepository.save(photo);

        // 4. 태그 저장
        dto.getTags().forEach(tagName -> {
            Tag tag = new Tag();
            tag.setTagName(tagName);
            tag.setPost(savedPost);
            tagRepository.save(tag);
        });

        return savedPost.getPostId().longValue();
    }
}
