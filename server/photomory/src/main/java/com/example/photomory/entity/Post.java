package com.example.photomory.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "POST")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "post_description", nullable = false)
    private String postDescription;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EveryPhoto> photos;

    public Post() {}

    // Getter/Setter
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<EveryPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<EveryPhoto> photos) {
        this.photos = photos;
    }
}
