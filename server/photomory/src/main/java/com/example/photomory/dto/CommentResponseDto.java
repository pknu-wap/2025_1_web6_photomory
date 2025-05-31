package com.example.photomory.dto;

import com.example.photomory.entity.Comment;


import java.time.format.DateTimeFormatter;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
public class CommentResponseDto {

    private Integer commentId;       // ✅ 댓글 고유 ID
    private Integer albumId;         // Comment가 직접 Album을 참조할 때
    private Integer postId;
    private Long userId;
    private String userName;         // 유저 이름
    private String commentsText;     // 엔티티 필드명과 일치
    private String createdAt;        // 작성 시간 (엔티티 필드명과 일치)

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
        Integer albumId = null;
        if (comment.getOurAlbum() != null) {
            albumId = comment.getOurAlbum().getAlbumId();
        } else if (comment.getOurPost() != null && comment.getOurPost().getOurAlbum() != null) {
            albumId = comment.getOurPost().getOurAlbum().getAlbumId();
        }

        return new CommentResponseDto(
                comment.getCommentId(),
                albumId,
                comment.getOurPost() != null ? comment.getOurPost().getPostId() : null,
                comment.getUser() != null ? comment.getUser().getUserId() : null,
                comment.getUser() != null ? comment.getUser().getUserName() : null,
                comment.getCommentText(),
                comment.getCommentTime() != null
                        ? comment.getCommentTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null
        );
    }


    // Getters & Setters (Lombok @Getter/@Setter 사용 시 불필요)
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