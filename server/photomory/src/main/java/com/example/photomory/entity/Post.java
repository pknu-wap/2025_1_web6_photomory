package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "POST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "post_description")
    private String postDescription;

    @Column(name = "location")
    private String location;

    @Column(name = "making_time")
    private LocalDateTime makingTime;

    @Builder.Default
    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @Builder.Default
    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public void setAlbum(Album album) {
        if (this.album != null && this.album.getPosts().contains(this)) {
            this.album.getPosts().remove(this);
        }
        this.album = album;
        if (album != null && !album.getPosts().contains(this)) {
            album.getPosts().add(this);
        }
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        if (photo.getPost() != this) {
            photo.setPost(this);
        }
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getPost() != this) {
            comment.setPost(this);
        }
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        if (tag != null && !tag.getPosts().contains(this)) {
            tag.getPosts().add(this);
        }
    }
}