package com.example.photomory.service;

import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.entity.Photo;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Tag;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.entity.Comment;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.PhotoRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.TagRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final PostRepository postRepository;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<EveryPostResponseDto> getAllPostsWithComments() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            EveryPostResponseDto dto = new EveryPostResponseDto();

            dto.setPostId(post.getPostId());
            dto.setUserId(post.getUser().getUserId());
            dto.setUserName(post.getUser().getUserName());
            dto.setUserPhotourl(post.getUser().getUserPhotourl());
            dto.setPostText(post.getPostText());
            dto.setPostDescription(post.getPostDescription());
            dto.setLikesCount(post.getLikesCount());
            dto.setLocation(post.getLocation());
            dto.setCreatedAt(post.getCreatedAt());


            Photo photo = photoRepository.findByPost(post).orElse(null);
            dto.setPhotoUrl(photo != null ? photo.getPhotoUrl() : null);


            List<String> tags = tagRepository.findByPost(post).stream()
                    .map(Tag::getTagName)
                    .toList();
            dto.setTags(tags);


            List<EveryCommentDto> commentDtos = commentRepository.findByPostId(post.getPostId()).stream()
                    .map(comment -> {
                        Long commenterId = comment.getUserId() != null ? comment.getUserId().longValue() : null;
                        UserEntity commenter = commenterId != null
                                ? userRepository.findById(commenterId).orElse(null)
                                : null;

                        return EveryCommentDto.builder()
                                .userId(commenterId)
                                .userName(commenter != null ? commenter.getUserName() : "알 수 없음")
                                .userPhotourl(commenter != null ? commenter.getUserPhotourl() : null)
                                .comment(comment.getCommentsText())
                                .createdAt(comment.getCreatedAt())  // createdAt이 comment 엔티티에 있어야 함
                                .build();
                    })
                    .toList();

            dto.setComments(commentDtos);
            dto.setCommentCount(commentDtos.size());


            return dto;
        }).toList();
    }
    public void createPost(EveryPostRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));


        Post post = Post.builder()
                .user(user)
                .postText(dto.getPostText())
                .postDescription(dto.getPostDescription())
                .location(dto.getLocation())
                .likesCount(0)
                .build();
        postRepository.save(post);

        Photo photo = Photo.builder()
                .post(post)
                .photoUrl(dto.getPhotoUrl())
                .photoName(dto.getPhotoName())
                .photoComment(dto.getPhotoComment())
                .photoMakingTime(dto.getPhotoMakingTime())
                .build();
        photoRepository.save(photo);

        for (String tagName : dto.getTags()) {
            Tag tag = Tag.builder()
                    .post(post)
                    .tagName(tagName)
                    .build();
            tagRepository.save(tag);
        }
    }


}
