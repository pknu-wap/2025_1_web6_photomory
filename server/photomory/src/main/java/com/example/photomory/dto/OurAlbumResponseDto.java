package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Photo; // Photo 엔티티 임포트 추가
import com.example.photomory.entity.UserEntity;

import lombok.AllArgsConstructor; // AllArgsConstructor 추가
import lombok.Getter; // Getter 추가
import lombok.NoArgsConstructor; // NoArgsConstructor 추가
import lombok.Setter; // Setter 추가 (특히 DTO에 값을 설정할 때 필요)

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Lombok 어노테이션 적용으로 코드 간결화 및 유지보수성 향상
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 필요하다면 (모든 필드를 포함하는 생성자)
public class OurAlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumDescription;
    private List<String> albumTags;
    private String albumMakingTime;

    private String userName;
    private String userPhotoUrl;

    private List<PostSummaryDto> posts;

    // fromEntity 메서드에서 Lombok이 생성한 Setter를 사용합니다.
    public static OurAlbumResponseDto fromEntity(Album album, List<Post> posts, UserEntity albumCreator) {
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

    // 기존 수동 Getters/Setters는 Lombok 어노테이션 (@Getter, @Setter) 사용 시 삭제 가능합니다.
    // 유지하더라도 Lombok이 생성한 메서드가 우선합니다.
    // 여기서는 가독성을 위해 주석 처리하거나 제거하는 것을 추천합니다.
    /*
    public Integer getAlbumId() { return albumId; }
    public void setAlbumId(Integer albumId) { this.albumId = albumId; }
    // ... 나머지 Getters / Setters
    */


    // 게시물 + 댓글 요약 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor // 필요하다면
    public static class PostSummaryDto {
        private Integer postId;
        private String postText;
        private String photoUrl; // 대표 사진 URL
        private List<CommentDto> comments;

        // fromEntity 메서드에서 Post 엔티티의 변경된 구조를 반영합니다.
        public static PostSummaryDto fromEntity(Post post) {
            PostSummaryDto dto = new PostSummaryDto();
            dto.setPostId(post.getPostId());
            dto.setPostText(post.getPostText());

            // 🚨 핵심 수정 부분: Post 엔티티의 photos 컬렉션에서 첫 번째 Photo의 URL을 가져옵니다.
            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                // Set은 순서를 보장하지 않으므로, '첫 번째'라는 의미는 Set의 이터레이터가 반환하는 첫 요소입니다.
                // 만약 특정 기준의 대표 사진(예: 썸네일 플래그)이 있다면 해당 로직을 구현해야 합니다.
                Photo firstPhoto = post.getPhotos().iterator().next();
                dto.setPhotoUrl(firstPhoto.getPhotoUrl());
            } else {
                dto.setPhotoUrl(null); // 게시물에 사진이 없을 경우 URL을 null로 설정
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

        // 기존 수동 Getters/Setters는 Lombok 어노테이션 사용 시 삭제 가능합니다.
        /*
        public Integer getPostId() { return postId; }
        public void setPostId(Integer postId) { this.postId = postId; }
        // ... 나머지 Getters / Setters
        */
    }

    // 댓글 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor // 필요하다면
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

        // 기존 수동 Getters/Setters는 Lombok 어노테이션 사용 시 삭제 가능합니다.
        /*
        public Integer getCommentId() { return commentId; }
        public void setCommentId(Integer commentId) { this.commentId = commentId; }
        // ... 나머지 Getters / Setters
        */
    }
}