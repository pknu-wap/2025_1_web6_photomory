package com.example.photomory.dto;

import com.example.photomory.entity.Comment;

public class CommentResponseDto {

    private Integer albumId;
    private Integer postId;
    private Long userId;             // Integer -> Long 변경
    private String commentsText;

    public CommentResponseDto() {}

    public CommentResponseDto(Integer albumId, Integer postId, Long userId, String commentsText) {
        this.albumId = albumId;
        this.postId = postId;
        this.userId = userId;
        this.commentsText = commentsText;
    }

    // Entity -> DTO 변환 메서드
    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getAlbum() != null ? comment.getAlbum().getAlbumId() : null,
                comment.getPost() != null ? comment.getPost().getPostId() : null,
                comment.getUser() != null ? comment.getUser().getUserId() : null,
                comment.getCommentsText()
        );
    }

    // Getter & Setter
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

    public Long getUserId() {            // Long으로 변경
        return userId;
    }

    public void setUserId(Long userId) { // Long으로 변경
        this.userId = userId;
    }

    public String getCommentsText() {
        return commentsText;
    }

    public void setCommentsText(String commentsText) {
        this.commentsText = commentsText;
    }
}
