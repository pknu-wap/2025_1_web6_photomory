package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import com.example.photomory.entity.OurAlbum;
import com.example.photomory.entity.OurPost;
import com.example.photomory.entity.Photo;
import com.example.photomory.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OurAlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumDescription;
    private List<String> albumTags;
    private String albumMakingTime;

    private String userName;
    private String userPhotoUrl;

    private List<PostSummaryDto> posts;

    public static OurAlbumResponseDto fromEntity(OurAlbum album, List<OurPost> posts, UserEntity albumCreator) {
        OurAlbumResponseDto dto = new OurAlbumResponseDto();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumName(album.getAlbumName());
        dto.setAlbumDescription(album.getAlbumDescription());

        if (album.getAlbumTag() != null && !album.getAlbumTag().isEmpty()) {
            dto.setAlbumTags(List.of(album.getAlbumTag()));
        } else {
            dto.setAlbumTags(Collections.emptyList());
        }

        if (album.getAlbumMakingTime() != null) {
            dto.setAlbumMakingTime(album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        if (albumCreator != null) {
            dto.setUserName(albumCreator.getUserName());
            dto.setUserPhotoUrl(albumCreator.getUserPhotourl());
        }

        if (posts != null && !posts.isEmpty()) {
            dto.setPosts(posts.stream()
                    .map(PostSummaryDto::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setPosts(Collections.emptyList());
        }

        return dto;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSummaryDto {
        private Integer postId;
        private String postText;
        private String photoUrl;
        private List<CommentDto> comments;

        public static PostSummaryDto fromEntity(OurPost post) {
            PostSummaryDto dto = new PostSummaryDto();
            dto.setPostId(post.getPostId());
            dto.setPostText(post.getPostText());

            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                Photo firstPhoto = post.getPhotos().iterator().next();
                dto.setPhotoUrl(firstPhoto.getPhotoUrl());
            } else {
                dto.setPhotoUrl(null);
            }

            if (post.getComments() != null) {
                dto.setComments(post.getComments().stream()
                        .map(CommentDto::fromEntity)
                        .collect(Collectors.toList()));
            } else {
                dto.setComments(Collections.emptyList());
            }

            return dto;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto {
        private Integer commentId;
        private String commentsText;
        private String userName;

        public static CommentDto fromEntity(Comment comment) {
            CommentDto dto = new CommentDto();
            dto.setCommentId(comment.getCommentId());
            dto.setCommentsText(comment.getCommentText());
            if (comment.getUser() != null) {
                dto.setUserName(comment.getUser().getUserName());
            } else {
                dto.setUserName("Unknown User");
            }
            return dto;
        }
    }
}
