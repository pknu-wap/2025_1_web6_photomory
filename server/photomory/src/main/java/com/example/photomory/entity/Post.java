package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
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

    // User와 ManyToOne 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Album과 ManyToOne 연관관계 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

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

    public void setAlbum(Album album) {
        this.album = album;
    }
}
