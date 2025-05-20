package com.example.photomory.service;

import com.example.photomory.dto.EveryPostResponseDto;
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

            Photo photo = photoRepository.findByPost(post).orElse(null);
            dto.setPhotoUrl(photo != null ? photo.getPhotoUrl() : null);

            List<String> tags = tagRepository.findByPost(post).stream()
                    .map(Tag::getTagName)
                    .toList();
            dto.setTags(tags);

            List<EveryCommentDto> commentDtos = commentRepository.findByPostId(post.getPostId()).stream()
                    .map(comment -> {
                        EveryCommentDto cdto = new EveryCommentDto();

                        UserEntity commenter = comment.getUser();

                        cdto.setUserId(commenter != null ? commenter.getUserId() : null);
                        cdto.setUserName(commenter != null ? commenter.getUserName() : "알 수 없음");
                        cdto.setUserPhotourl(commenter != null ? commenter.getUserPhotourl() : null);
                        cdto.setCommentText(comment.getCommentsText());
                        return cdto;
                    }).toList();

            dto.setComments(commentDtos);
            dto.setCommentCount(commentDtos.size());

            return dto;
        }).toList();
    }
}
