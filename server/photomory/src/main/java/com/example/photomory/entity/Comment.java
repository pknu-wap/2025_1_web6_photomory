package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "COMMENT")
@IdClass(Comment.CommentId.class)
public class Comment {

    @Id
    @Column(name = "album_id", nullable = false)
    private Integer albumId;

    @MapsId("albumId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", insertable = false, updatable = false)
    private Album album;

    @Id
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "comments_text", nullable = false)
    private String commentsText;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    public Comment() {}

    // getter, setter
    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentsText() {
        return commentsText;
    }

    public void setCommentsText(String commentsText) {
        this.commentsText = commentsText;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    // Composite Key Class
    public static class CommentId implements Serializable {
        private Integer albumId;  // Integer로 맞춤
        private Integer postId;

        public CommentId() {}

        public CommentId(Integer albumId, Integer postId) {
            this.albumId = albumId;
            this.postId = postId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CommentId)) return false;
            CommentId that = (CommentId) o;
            return Objects.equals(albumId, that.albumId) &&
                    Objects.equals(postId, that.postId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(albumId, postId);
        }
    }
}
