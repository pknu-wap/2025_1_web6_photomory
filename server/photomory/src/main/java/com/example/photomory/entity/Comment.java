package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "COMMENTS")
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // postId를 자동생성 하려면 필요 (필요 없으면 제거)
    private Integer postId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "comments_text", nullable = false)
    private String commentsText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Comment() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Composite Key Class
    public static class CommentId implements Serializable {
        private Integer albumId;
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
