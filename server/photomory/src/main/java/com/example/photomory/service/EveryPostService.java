package com.example.photomory.service;

import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.dto.EveryCommentDto; // EveryCommentDto 임포트
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Tag;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.entity.Comment; // Comment 엔티티 임포트
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.TagRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EveryPostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository; // 사용되지 않는 경고는 무시하거나, UserEntity를 직접 조회하지 않는다면 제거를 고려할 수 있습니다.

    public List<EveryPostResponseDto> getAllPostsWithComments() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            EveryPostResponseDto dto = new EveryPostResponseDto();

            dto.setPostId(post.getPostId());
            // UserEntity 정보는 Post 엔티티에 직접 연결되어 있으므로 직접 접근합니다.
            // null 체크를 추가하여 NPE 방지
            if (post.getUser() != null) {
                dto.setUserId(post.getUser().getUserId());
                dto.setUserName(post.getUser().getUserName());
                dto.setUserPhotourl(post.getUser().getUserPhotourl());
            } else {
                // 사용자 정보가 없을 경우 기본값 설정 또는 예외 처리
                dto.setUserId(null);
                dto.setUserName("Unknown User");
                dto.setUserPhotourl(null);
            }

            dto.setPostText(post.getPostText());
            dto.setPostDescription(post.getPostDescription());
            dto.setLikesCount(post.getLikesCount());
            dto.setLocation(post.getLocation());

            // Post 엔티티에 직접 photoUrl 필드가 있으므로 사용
            dto.setPhotoUrl(post.getPhotoUrl());

            // Post 엔티티의 tags 필드 (Set<Tag>)에서 tagName을 추출하여 설정
            if (post.getTags() != null) {
                dto.setTags(post.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()));
            } else {
                dto.setTags(Collections.emptyList());
            }


            // 댓글 조회 로직은 그대로 유지 (findByPost_PostId는 Long ID를 받음)
            List<EveryCommentDto> commentDtos = commentRepository.findByPost_PostId(post.getPostId().longValue()).stream()
                    .map(comment -> {
                        EveryCommentDto cdto = new EveryCommentDto();

                        UserEntity commenter = comment.getUser();

                        cdto.setUserId(commenter != null ? commenter.getUserId() : null);
                        cdto.setUserName(commenter != null ? commenter.getUserName() : "알 수 없음");
                        cdto.setUserPhotourl(commenter != null ? commenter.getUserPhotourl() : null);
                        cdto.setCommentText(comment.getCommentsText()); // Comment 엔티티 필드명 일치
                        return cdto;
                    }).toList();

            dto.setComments(commentDtos);
            dto.setCommentCount(commentDtos.size());

            return dto;
        }).toList();
    }
}