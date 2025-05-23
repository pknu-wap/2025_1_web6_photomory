package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Tag;
import com.example.photomory.entity.UserEntity; // UserEntity 임포트 추가

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Lombok 어노테이션을 사용하여 코드를 간결하게 만드는 것을 권장합니다.
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor // 필요하다면
public class OurAlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumDescription;
    private List<String> albumTags; // 여러 개의 태그를 담을 리스트 필드 추가
    private String albumMakingTime;

    private String userName;
    private String userPhotoUrl;

    private List<PostSummaryDto> posts; // 이 posts는 서비스에서 채워져야 함

    public OurAlbumResponseDto() {}

    // fromEntity 메서드의 시그니처 변경: posts 목록과 UserEntity를 추가로 받습니다.
    // Album 엔티티는 Posts를 직접 포함하지 않으므로, 서비스에서 조회한 Posts를 넘겨줘야 합니다.
    public static OurAlbumResponseDto fromEntity(Album album, List<Post> posts, UserEntity albumCreator) {
        OurAlbumResponseDto dto = new OurAlbumResponseDto();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumName(album.getAlbumName());
        dto.setAlbumDescription(album.getAlbumDescription());

        // album.getAlbumTags()는 Set<Tag>를 반환하므로, 이를 List<String>으로 변환합니다.
        if (album.getAlbumTags() != null) {
            dto.setAlbumTags(album.getAlbumTags().stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList()));
        } else {
            dto.setAlbumTags(Collections.emptyList());
        }

        if (album.getAlbumMakingTime() != null) {
            dto.setAlbumMakingTime(album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        // 앨범 생성자 정보 설정
        if (albumCreator != null) {
            dto.setUserName(albumCreator.getUserName());
            dto.setUserPhotoUrl(albumCreator.getUserPhotourl());
        }

        // 게시물 목록 설정 (서비스에서 전달받은 posts 사용)
        if (posts != null && !posts.isEmpty()) {
            dto.setPosts(posts.stream()
                    .map(PostSummaryDto::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setPosts(Collections.emptyList());
        }

        return dto;
    }

    // Getters / Setters (Lombok @Getter/@Setter 사용 시 필요 없음, 하지만 요청에 따라 유지)
    public Integer getAlbumId() { return albumId; }
    public void setAlbumId(Integer albumId) { this.albumId = albumId; }
    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    public String getAlbumDescription() { return albumDescription; }
    public void setAlbumDescription(String albumDescription) { this.albumDescription = albumDescription; }
    public List<String> getAlbumTags() { return albumTags; }
    public void setAlbumTags(List<String> albumTags) { this.albumTags = albumTags; }
    public String getAlbumMakingTime() { return albumMakingTime; }
    public void setAlbumMakingTime(String albumMakingTime) { this.albumMakingTime = albumMakingTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserPhotoUrl() { return userPhotoUrl; }
    public void setUserPhotoUrl(String userPhotoUrl) { this.userPhotoUrl = userPhotoUrl; }
    public List<PostSummaryDto> getPosts() { return posts; }
    public void setPosts(List<PostSummaryDto> posts) { this.posts = posts; }


    // 게시물 + 댓글 요약 DTO
    public static class PostSummaryDto {
        private Integer postId;
        private String postText;
        private String photoUrl;
        private List<CommentDto> comments;

        public PostSummaryDto() {}

        public static PostSummaryDto fromEntity(Post post) {
            PostSummaryDto dto = new PostSummaryDto();
            dto.setPostId(post.getPostId());
            dto.setPostText(post.getPostText());

            // Post 엔티티에 직접 photoUrl 필드가 있으므로 Photos 리스트를 순회할 필요 없음
            dto.setPhotoUrl(post.getPhotoUrl()); // Post.getPhotoUrl() 사용

            if (post.getComments() != null) {
                dto.setComments(post.getComments().stream()
                        .map(CommentDto::fromEntity)
                        .collect(Collectors.toList()));
            } else {
                dto.setComments(Collections.emptyList());
            }

            return dto;
        }

        // Getters / Setters (Lombok @Getter/@Setter 사용 시 필요 없음, 하지만 요청에 따라 유지)
        public Integer getPostId() { return postId; }
        public void setPostId(Integer postId) { this.postId = postId; }
        public String getPostText() { return postText; }
        public void setPostText(String postText) { this.postText = postText; }
        public String getPhotoUrl() { return photoUrl; }
        public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
        public List<CommentDto> getComments() { return comments; }
        public void setComments(List<CommentDto> comments) { this.comments = comments; }
    }

    // 댓글 DTO
    public static class CommentDto {
        private Integer commentId;
        private String commentsText;
        private String userName;

        public static CommentDto fromEntity(Comment comment) {
            CommentDto dto = new CommentDto();
            dto.setCommentId(comment.getCommentId());
            dto.setCommentsText(comment.getCommentsText());
            if (comment.getUser() != null) {
                dto.setUserName(comment.getUser().getUserName());
            } else {
                dto.setUserName("Unknown User");
            }
            return dto;
        }

        // Getters / Setters (Lombok @Getter/@Setter 사용 시 필요 없음, 하지만 요청에 따라 유지)
        public Integer getCommentId() { return commentId; }
        public void setCommentId(Integer commentId) { this.commentId = commentId; }
        public String getCommentsText() { return commentsText; }
        public void setCommentsText(String commentsText) { this.commentsText = commentsText; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
    }
}