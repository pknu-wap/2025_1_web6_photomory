package com.example.photomory.service;

import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.entity.EveryPost;
import com.example.photomory.entity.Tag;
import com.example.photomory.repository.EveryPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final EveryPostRepository everyPostRepository;

    public List<EveryPostResponseDto> getAllPosts() {
        List<EveryPost> posts = everyPostRepository.findAllWithUserAndComments();

        return posts.stream().map(post -> EveryPostResponseDto.builder()
                .postId(post.getPostId().longValue())
                .userId(post.getUser().getUserId().longValue())
                .userName(post.getUser().getUserName())
                .userPhotourl(post.getUser().getUserPhotourl())
                .postText(post.getPostText())
                .postDescription(post.getPostDescription())
                .location(post.getLocation())
                .photoUrl(post.getPhotos().isEmpty() ? null : post.getPhotos().get(0).getPhotoUrl())
                .likesCount(post.getLikesCount())
                .tags(post.getTags().stream().map(Tag::getTagName).toList())
                .commentCount(post.getComments().size())
                .comments(post.getComments().stream().map(comment -> EveryCommentDto.builder()
                        .userId(comment.getUser().getUserId().longValue())
                        .userName(comment.getUser().getUserName())
                        .userPhotourl(comment.getUser().getUserPhotourl())
                        .commentText(comment.getCommentText())
                        .build()).toList())
                .build()
        ).toList();
    }
}
