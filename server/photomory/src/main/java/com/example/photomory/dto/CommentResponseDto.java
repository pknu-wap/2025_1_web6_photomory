package com.example.photomory.dto;

import com.example.photomory.entity.Comment;

import java.time.format.DateTimeFormatter;

public class CommentResponseDto {

    private Integer commentId;       // ✅ 댓글 고유 ID
    private Integer albumId;
    private Integer postId;
    private Long userId;
    private String userName;         // 유저 이름
    private String commentsText;
    private String createdAt;        // 작성 시간

    public CommentResponseDto() {}

    public CommentResponseDto(Integer commentId, Integer albumId, Integer postId, Long userId,
                              String userName, String commentsText, String createdAt) {
        this.commentId = commentId;
        this.albumId = albumId;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.commentsText = commentsText;
        this.createdAt = createdAt;
    }

    // Entity -> DTO 변환 메서드
    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getAlbum() != null ? comment.getAlbum().getAlbumId().intValue() : null,
                comment.getPost() != null ? comment.getPost().getPostId() : null,
                comment.getUser() != null ? comment.getUser().getUserId() : null,
                comment.getUser() != null ? comment.getUser().getUserName() : null,
                comment.getCommentsText(),
                comment.getCreatedAt() != null
                        ? comment.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null
        );
    }

    // Getters & Setters
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentsText() {
        return commentsText;
    }

    public void setCommentsText(String commentsText) {
        this.commentsText = commentsText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
