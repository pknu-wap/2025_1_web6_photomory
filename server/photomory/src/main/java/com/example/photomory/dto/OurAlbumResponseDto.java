package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OurAlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumDescription;
    private String albumTag;
    private String albumMakingTime;

    private String userName;
    private String userPhotoUrl;

    private List<PostSummaryDto> posts;

    public OurAlbumResponseDto() {}

    public static OurAlbumResponseDto fromEntity(Album album) {
        OurAlbumResponseDto dto = new OurAlbumResponseDto();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumName(album.getAlbumName());
        dto.setAlbumDescription(album.getAlbumDescription());
        dto.setAlbumTag(album.getAlbumTag());

        if (album.getAlbumMakingTime() != null) {
            dto.setAlbumMakingTime(album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        if (album.getPosts() != null && !album.getPosts().isEmpty()) {
            Post firstPost = album.getPosts().get(0);
            if (firstPost.getUser() != null) {
                dto.setUserName(firstPost.getUser().getUserName());
                dto.setUserPhotoUrl(firstPost.getUser().getUserPhotourl());
            }

            dto.setPosts(album.getPosts().stream()
                    .map(PostSummaryDto::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // Getters / Setters
    public Integer getAlbumId() { return albumId; }
    public void setAlbumId(Integer albumId) { this.albumId = albumId; }
    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    public String getAlbumDescription() { return albumDescription; }
    public void setAlbumDescription(String albumDescription) { this.albumDescription = albumDescription; }
    public String getAlbumTag() { return albumTag; }
    public void setAlbumTag(String albumTag) { this.albumTag = albumTag; }
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

            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                dto.setPhotoUrl(post.getPhotos().get(0).getPhotoUrl());
            }

            if (post.getComments() != null) {
                dto.setComments(post.getComments().stream()
                        .map(CommentDto::fromEntity)
                        .collect(Collectors.toList()));
            }

            return dto;
        }

        // Getters / Setters
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
        private Long commentId;          // Long으로 변경
        private String commentsText;
        private String userName;

        public static CommentDto fromEntity(Comment comment) {
            CommentDto dto = new CommentDto();
            dto.setCommentId(comment.getCommentId());
            dto.setCommentsText(comment.getCommentsText());
            dto.setUserName(comment.getUser().getUserName());
            return dto;
        }

        // Getters / Setters
        public Long getCommentId() { return commentId; }
        public void setCommentId(Long commentId) { this.commentId = commentId; }
        public String getCommentsText() { return commentsText; }
        public void setCommentsText(String commentsText) { this.commentsText = commentsText; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
    }
}
