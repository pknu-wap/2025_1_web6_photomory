package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @Column(name = "post_description", nullable = false)
    private String postDescription;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 🔧 Setter
    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
